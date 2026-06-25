/* =========================================================
   MacroPulse — GDP Race
   Renders an animated bar-chart race from World Bank GDP
   history. Years, countries, and rankings are all derived
   from the fetched data, so adding rows to the CSV (new
   years or countries) requires no code changes here.
========================================================= */

const RACE_CONFIG = {
    barHeight: 40,
    rowHeight: 60,
    barStartX: 320,
    barMaxWidth: 800,
    chartWidth: 1400,
    chartTopPadding: 100,
    medalColors: ["#FFD700", "#C0C0C0", "#CD7F32"],
    defaultBarColor: "#19d3f3",
    highlightColor: "#FF00FF",
    growthUpColor: "#00FF7F",
    growthDownColor: "#FF6B6B",
    transitionMs: 1000,
    moversShown: 5,
    excludedNameFragments: [
        "world", "income", "oecd", "euro area", "sub-saharan",
        "africa eastern", "africa western", "least developed",
        "fragile", "arab world", "latin america", "caribbean",
        "north america", "middle east", "europe", "asia",
        "ida", "ibrd", "hipc", "small states", "classification",
        "dividend", "members"
    ],
    countryNameAliases: {
        "Russian Federation": "Russia",
        "Korea, Rep.": "South Korea",
        "United Kingdom": "UK"
    }
};

const ISO3_TO_ISO2 = {
    USA:"US", DEU:"DE", GBR:"GB", CHN:"CN", IND:"IN", JPN:"JP",
    RUS:"RU", KOR:"KR", TUR:"TR", CHE:"CH", NLD:"NL", SWE:"SE",
    NOR:"NO", DNK:"DK", FIN:"FI", AUT:"AT", BEL:"BE", IRL:"IE",
    PRT:"PT", FRA:"FR", ITA:"IT", ESP:"ES", CAN:"CA", BRA:"BR",
    AUS:"AU", MEX:"MX", SAU:"SA", IDN:"ID", ARG:"AR", ZAF:"ZA",
    EGY:"EG", PAK:"PK", BGD:"BD", THA:"TH", VNM:"VN", MYS:"MY",
    SGP:"SG", PHL:"PH", NZL:"NZ", IRN:"IR", IRQ:"IQ", ISR:"IL",
    UKR:"UA", POL:"PL", CZE:"CZ", HUN:"HU", ROU:"RO", GRC:"GR",
    CHL:"CL", COL:"CO", PER:"PE", VEN:"VE", NGA:"NG", ETH:"ET",
    DZA:"DZ", MAR:"MA", KAZ:"KZ", ARE:"AE", QAT:"QA", KWT:"KW",
    OMN:"OM", BHR:"BH", LKA:"LK", NPL:"NP", MMR:"MM", KHM:"KH",
    LAO:"LA", BRN:"BN", HKG:"HK", MAC:"MO", TWN:"TW", ISL:"IS",
    LUX:"LU"
};

function countryCodeToFlag(code) {
    const iso2 = ISO3_TO_ISO2[code];
    if (!iso2) return "🏳️";
    return iso2.replace(
        /./g,
        c => String.fromCodePoint(127397 + c.charCodeAt(0))
    );
}

function isExcludedRegion(countryName) {
    const c = countryName.toLowerCase();
    return RACE_CONFIG.excludedNameFragments.some(f => c.includes(f));
}

function displayName(country) {
    return RACE_CONFIG.countryNameAliases[country] || country;
}

let historyData = [];
let years = [];
let historyChart = null;
let compareChart = null;
let svg = null;
let playing = false;
let playTimer = null;

let dom = {};

function cacheDom() {
    dom = {
        yearSelector: document.getElementById("yearSelector"),
        startYear: document.getElementById("startYear"),
        endYear: document.getElementById("endYear"),
        speedSelector: document.getElementById("speedSelector"),
        topSelector: document.getElementById("topSelector"),
        countrySearch: document.getElementById("countrySearch"),
        currentYear: document.getElementById("currentYear"),
        country1: document.getElementById("country1"),
        country2: document.getElementById("country2"),
        compareBtn: document.getElementById("compareBtn"),
        playBtn: document.getElementById("playBtn"),
        resetBtn: document.getElementById("resetBtn"),
        downloadBtn: document.getElementById("downloadBtn"),
        topMovers: document.getElementById("topMovers"),
        chartTitle: document.getElementById("chartTitle"),
        historyPanel: document.getElementById("historyPanel"),
        historyBackdrop: document.getElementById("historyBackdrop"),
        closeHistory: document.getElementById("closeHistory"),
        chartScroll: document.getElementById("chartScroll"),
        historyCanvas: document.getElementById("countryHistoryChart")
    };
}

async function startGdpRace() {

    historyData = await fetch("/worldbank-gdp").then(r => r.json());

    years = [...new Set(historyData.map(x => x.year))]
        .sort((a, b) => a - b);

    cacheDom();
    populateYearControls();
    populateCountrySelectors();
    buildSvgCanvas();
    wireEvents();

    const latestYear = years[years.length - 1];
    dom.yearSelector.value = latestYear;
    drawYear(latestYear);
}

function populateYearControls() {

    dom.yearSelector.min = years[0];
    dom.yearSelector.max = years[years.length - 1];

    years.forEach(year => {
        dom.startYear.add(new Option(year, year));
        dom.endYear.add(new Option(year, year));
    });

    dom.startYear.value = years[0];
    dom.endYear.value = years[years.length - 1];
}

function populateCountrySelectors() {

    const countryNames = [...new Set(
        historyData
            .filter(x => !isExcludedRegion(x.country))
            .map(x => x.country)
    )].sort();

    countryNames.forEach(c => {
        dom.country1.add(new Option(displayName(c), c));
        dom.country2.add(new Option(displayName(c), c));
    });

    if (countryNames.length > 1) {
        dom.country1.value = countryNames[0];
        dom.country2.value = countryNames[1];
    }
}

function buildSvgCanvas() {
    svg = d3.select("#economyRace2")
        .append("svg")
        .attr("width", RACE_CONFIG.chartWidth)
        .attr("height", RACE_CONFIG.chartTopPadding);
}

function countriesForYear(year, limit) {
    return historyData
        .filter(x => x.year === year)
        .filter(x => x.gdp > 0)
        .filter(x => !isExcludedRegion(x.country))
        .sort((a, b) => b.gdp - a.gdp)
        .slice(0, limit);
}

function gdpForCountryYear(country, year) {
    return historyData.find(
        x => x.country === country && x.year === year
    );
}

function drawYear(year) {

    dom.currentYear.innerHTML = year;

    const limit = Number(dom.topSelector.value);
    const countries = countriesForYear(year, limit);

    svg.attr("height", countries.length * RACE_CONFIG.rowHeight + RACE_CONFIG.chartTopPadding);

    const previousCountries = countriesForYear(year - 1, Infinity);
    const worldGdp = d3.sum(countries, d => d.gdp);
    const searchTerm = dom.countrySearch.value.trim().toLowerCase();

    const scale = d3.scaleLinear()
        .domain([0, d3.max(countries, d => d.gdp) || 1])
        .range([0, RACE_CONFIG.barMaxWidth]);

    drawBars(countries, scale, searchTerm);
    drawCountryLabels(countries, previousCountries);
    drawGdpLabels(countries, year, worldGdp);
    renderTopMovers(countries, year);
}

function drawBars(countries, scale, searchTerm) {

    svg.selectAll("rect")
        .data(countries, d => d.country)
        .join("rect")
        .style("cursor", "pointer")
        .on("click", (event, d) => showCountryHistory(d.country))
        .transition()
        .duration(RACE_CONFIG.transitionMs)
        .ease(d3.easeLinear)
        .attr("x", RACE_CONFIG.barStartX)
        .attr("y", (d, i) => i * RACE_CONFIG.rowHeight)
        .attr("height", RACE_CONFIG.barHeight)
        .attr("rx", 6)
        .attr("width", d => scale(d.gdp))
        .attr("fill", (d, i) => barColor(d, i, searchTerm));
}

function barColor(d, i, searchTerm) {

    if (searchTerm && d.country.toLowerCase().includes(searchTerm)) {
        return RACE_CONFIG.highlightColor;
    }

    if (i < RACE_CONFIG.medalColors.length) {
        return RACE_CONFIG.medalColors[i];
    }

    return RACE_CONFIG.defaultBarColor;
}

function drawCountryLabels(countries, previousCountries) {

    svg.selectAll(".country")
        .data(countries, d => d.country)
        .join("text")
        .attr("class", "country")
        .style("cursor", "pointer")
        .on("click", (event, d) => showCountryHistory(d.country))
        .on("mouseover", function () { d3.select(this).style("fill", "#FFD700"); })
        .on("mouseout", function () { d3.select(this).style("fill", "white"); })
        .transition()
        .duration(RACE_CONFIG.transitionMs)
        .ease(d3.easeLinear)
        .attr("x", 30)
        .attr("y", (d, i) => i * RACE_CONFIG.rowHeight + 25)
        .text((d, i) => rankLabel(d, i, previousCountries))
        .attr("fill", "white")
        .style("font-size", "18px")
        .style("font-weight", "bold");
}

function rankLabel(d, i, previousCountries) {

    let medal = "";
    if (i === 0) medal = "🥇 ";
    else if (i === 1) medal = "🥈 ";
    else if (i === 2) medal = "🥉 ";

    const previousRank = previousCountries.findIndex(x => x.country === d.country);

    let movement = "➖";
    if (previousRank !== -1) {
        if (previousRank > i) movement = "▲";
        else if (previousRank < i) movement = "▼";
    }

    return `${medal}${movement} #${i + 1} ${countryCodeToFlag(d.countryCode)} ${displayName(d.country)}`;
}

function drawGdpLabels(countries, year, worldGdp) {

    const scale = d3.scaleLinear()
        .domain([0, d3.max(countries, d => d.gdp) || 1])
        .range([0, RACE_CONFIG.barMaxWidth]);

    svg.selectAll(".gdp")
        .data(countries, d => d.country)
        .join("text")
        .attr("class", "gdp")
        .transition()
        .duration(RACE_CONFIG.transitionMs)
        .ease(d3.easeLinear)
        .attr("x", d => RACE_CONFIG.barStartX + 10 + scale(d.gdp))
        .attr("y", (d, i) => i * RACE_CONFIG.rowHeight + 25)
        .text(d => gdpLabelText(d, worldGdp))
        .attr("fill", d => gdpLabelColor(d, year))
        .style("font-size", "18px")
        .style("font-weight", "bold");
}

function gdpLabelText(d, worldGdp) {
    const share = (d.gdp / worldGdp) * 100;
    return `$${d.gdp.toFixed(2)}T 🌍${share.toFixed(1)}%`;
}

function gdpLabelColor(d, year) {
    const previous = gdpForCountryYear(d.country, year - 1);
    if (!previous) return "#FFD700";
    return d.gdp >= previous.gdp
        ? RACE_CONFIG.growthUpColor
        : RACE_CONFIG.growthDownColor;
}

function renderTopMovers(countries, year) {

    const movers = countries
        .map(c => {
            const prev = gdpForCountryYear(c.country, year - 1);
            if (!prev || prev.gdp <= 0) return null;
            return {
                country: c.country,
                growth: ((c.gdp - prev.gdp) / prev.gdp) * 100
            };
        })
        .filter(Boolean)
        .sort((a, b) => b.growth - a.growth)
        .slice(0, RACE_CONFIG.moversShown);

    if (movers.length === 0) {
        dom.topMovers.innerHTML = `<p style="color:#7e93bb;">Not enough data for this year yet.</p>`;
        return;
    }

    const maxGrowth = Math.max(...movers.map(m => m.growth), 0.01);

    dom.topMovers.innerHTML = movers.map(m => `
        <div class="mover-row">
            <span class="mover-name">${displayName(m.country)}</span>
            <div class="mover-track">
                <div class="mover-fill" style="width:${Math.max(4, (m.growth / maxGrowth) * 100)}%"></div>
            </div>
            <span class="mover-value">+${m.growth.toFixed(1)}%</span>
        </div>
    `).join("");
}

function showCountryHistory(country) {

    dom.chartTitle.innerHTML = `${displayName(country)} GDP History`;
    dom.historyPanel.style.display = "block";
    dom.historyBackdrop.style.display = "block";

    const countryData = historyData.filter(x => x.country === country);
    const labels = countryData.map(x => x.year);
    const values = countryData.map(x => x.gdp);

    destroyHistoryCharts();

    const canvas = dom.historyCanvas;
    canvas.width = Math.max(8000, labels.length * 220);
    canvas.height = window.innerHeight * 0.75;

    historyChart = new Chart(canvas.getContext("2d"), {
        type: "line",
        data: {
            labels: labels,
            datasets: [singleCountryDataset(displayName(country), values, "#00d4ff", "rgba(0,212,255,0.2)")]
        },
        options: historyChartOptions(values)
    });

    setTimeout(() => {
        dom.chartScroll.scrollLeft = dom.chartScroll.scrollWidth;
    }, 100);
}

function showCompare(countryA, countryB) {

    if (!countryA || !countryB || countryA === countryB) {
        dom.chartTitle.innerHTML = "Pick two different countries to compare";
    } else {
        dom.chartTitle.innerHTML = `${displayName(countryA)} vs ${displayName(countryB)} — GDP History`;
    }

    dom.historyPanel.style.display = "block";
    dom.historyBackdrop.style.display = "block";

    const dataA = historyData.filter(x => x.country === countryA);
    const dataB = historyData.filter(x => x.country === countryB);

    const allYears = [...new Set([
        ...dataA.map(x => x.year),
        ...dataB.map(x => x.year)
    ])].sort((a, b) => a - b);

    const valuesA = allYears.map(y => {
        const match = dataA.find(x => x.year === y);
        return match ? match.gdp : null;
    });

    const valuesB = allYears.map(y => {
        const match = dataB.find(x => x.year === y);
        return match ? match.gdp : null;
    });

    destroyHistoryCharts();

    const canvas = dom.historyCanvas;
    canvas.width = Math.max(8000, allYears.length * 220);
    canvas.height = window.innerHeight * 0.75;

    const combinedValues = [...valuesA, ...valuesB].filter(v => v !== null);

    compareChart = new Chart(canvas.getContext("2d"), {
        type: "line",
        data: {
            labels: allYears,
            datasets: [
                singleCountryDataset(displayName(countryA), valuesA, "#00d4ff", "rgba(0,212,255,0.15)"),
                singleCountryDataset(displayName(countryB), valuesB, "#FFD700", "rgba(255,215,0,0.15)")
            ]
        },
        options: historyChartOptions(combinedValues, true)
    });

    setTimeout(() => {
        dom.chartScroll.scrollLeft = dom.chartScroll.scrollWidth;
    }, 100);
}

function singleCountryDataset(label, values, borderColor, backgroundColor) {
    return {
        label: `${label} GDP (Trillion USD)`,
        data: values,
        borderColor: borderColor,
        backgroundColor: backgroundColor,
        tension: 0.3,
        fill: true,
        pointRadius: 6,
        pointHoverRadius: 10,
        pointHitRadius: 18,
        spanGaps: true
    };
}

function historyChartOptions(values, showLegend) {
    return {
        layout: { padding: 0 },
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: "nearest", intersect: false },
        plugins: {
            title: { display: false },
            legend: { display: !!showLegend, labels: { color: "#ffffff" } }
        },
        scales: {
            x: {
                ticks: { color: "#ffffff", font: { size: 14 }, autoSkip: true, maxTicksLimit: 20 },
                grid: { color: "rgba(255,255,255,0.05)" }
            },
            y: {
                beginAtZero: false,
                grace: "5%",
                min: Math.min(...values) * 0.95,
                max: Math.max(...values) * 1.05,
                ticks: { color: "#ffffff", font: { size: 16 } },
                title: {
                    display: true,
                    text: "GDP (Trillion USD)",
                    color: "#ffffff",
                    font: { size: 18, weight: "bold" }
                },
                grid: { color: "rgba(255,255,255,0.1)" }
            }
        }
    };
}

function destroyHistoryCharts() {
    if (historyChart) { historyChart.destroy(); historyChart = null; }
    if (compareChart) { compareChart.destroy(); compareChart = null; }
}

function closeHistoryModal() {
    dom.historyPanel.style.display = "none";
    dom.historyBackdrop.style.display = "none";
}

function stopPlayback() {
    clearInterval(playTimer);
    playing = false;
    dom.playBtn.innerHTML = "▶ Play";
}

function startPlayback() {

    const start = Number(dom.startYear.value);
    const end = Number(dom.endYear.value);
    const filteredYears = years.filter(y => y >= start && y <= end);

    if (filteredYears.length === 0) return;

    let currentIndex = 0;
    dom.yearSelector.value = filteredYears[0];
    drawYear(filteredYears[0]);

    playing = true;
    dom.playBtn.innerHTML = "⏸ Pause";

    playTimer = setInterval(() => {

        currentIndex++;

        if (currentIndex >= filteredYears.length) {
            stopPlayback();
            return;
        }

        dom.yearSelector.value = filteredYears[currentIndex];
        drawYear(filteredYears[currentIndex]);

    }, Number(dom.speedSelector.value));
}

function downloadRaceAsPng() {
    html2canvas(document.getElementById("gdpSection"), {
        backgroundColor: "#0b1b3a",
        scale: 2
    }).then(canvas => {
        const link = document.createElement("a");
        link.download = "MacroPulse-GDP-Race.png";
        link.href = canvas.toDataURL("image/png");
        link.click();
    });
}

function wireEvents() {

    dom.yearSelector.addEventListener("input", () => {
        drawYear(Number(dom.yearSelector.value));
    });

    dom.countrySearch.addEventListener("input", () => {
        drawYear(Number(dom.yearSelector.value));
    });

    dom.topSelector.addEventListener("change", () => {
        drawYear(Number(dom.yearSelector.value));
    });

    dom.resetBtn.addEventListener("click", () => {
        stopPlayback();
        dom.yearSelector.value = years[0];
        drawYear(years[0]);
    });

    dom.playBtn.addEventListener("click", () => {
        if (!playing) startPlayback();
        else stopPlayback();
    });

    dom.downloadBtn.addEventListener("click", downloadRaceAsPng);

    dom.compareBtn.addEventListener("click", () => {
        showCompare(dom.country1.value, dom.country2.value);
    });

    dom.closeHistory.addEventListener("click", closeHistoryModal);
    dom.historyBackdrop.addEventListener("click", closeHistoryModal);
}

startGdpRace();
