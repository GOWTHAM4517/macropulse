/* =========================================================
   MacroPulse — Asset Detail Page Engine
   Every asset page (gold.html, bitcoin.html, ...) includes
   this file plus a single line setting window.ASSET_KEY.
   This script fills in the title/price/chart/stats and
   keeps polling for live updates.
========================================================= */

const DETAIL_POLL_MS = 10000;
const DETAIL_MAX_POINTS = 60;

let detailChart = null;
const sessionHistory = [];
const sessionLabels = [];

function initDetailPage() {

    const asset = ASSETS[window.ASSET_KEY];
    if (!asset) {
        console.error("Unknown asset key:", window.ASSET_KEY);
        return;
    }

    applyTheme(asset);
    paintStaticText(asset);
    buildDetailChart(asset);

    pollAsset(asset);
    setInterval(() => pollAsset(asset), DETAIL_POLL_MS);
}

function applyTheme(asset) {
    document.documentElement.style.setProperty("--asset-color", asset.color);
}

function paintStaticText(asset) {
    setText("assetIcon", asset.icon);
    setText("assetTitle", asset.title);
    setText("assetSubtitle", asset.subtitle);
}

function buildDetailChart(asset) {

    const ctx = document.getElementById("detailChart");

    detailChart = new Chart(ctx, {
        type: "line",
        data: {
            labels: sessionLabels,
            datasets: [{
                label: asset.title,
                data: sessionHistory,
                borderColor: asset.color,
                backgroundColor: hexToRgba(asset.color, 0.18),
                borderWidth: 3,
                tension: 0.35,
                fill: true,
                pointRadius: 0,
                pointHoverRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            interaction: { mode: "nearest", intersect: false },
            plugins: {
                legend: { display: false }
            },
            scales: {
                x: {
                    ticks: { color: "#9fb3d6", maxTicksLimit: 8 },
                    grid: { color: "rgba(255,255,255,0.05)" }
                },
                y: {
                    ticks: { color: "#9fb3d6" },
                    grid: { color: "rgba(255,255,255,0.08)" }
                }
            }
        }
    });
}

async function pollAsset(asset) {

    try {

        const data = await fetch(asset.endpoint).then(r => r.json());
        const price = data[asset.priceField];

        if (typeof price !== "number") return;

        updatePriceDisplay(asset, price, data.changePercent);
        updateStats(price, data.history);
        pushToChart(price);

        showLiveBadge(data.live !== false);

    } catch (error) {
        console.log(error);
        showLiveBadge(false);
    }
}

function updatePriceDisplay(asset, price, changePercent) {

    setText("assetPrice", formatAssetPrice(asset, price));

    const badge = document.getElementById("assetChange");
    if (!badge) return;

    if (typeof changePercent !== "number" || changePercent === 0) {
        badge.textContent = "—";
        badge.className = "change-badge large";
        return;
    }

    const arrow = changePercent > 0 ? "▲" : "▼";
    badge.textContent = `${arrow} ${Math.abs(changePercent).toFixed(2)}%`;
    badge.className = "change-badge large " + (changePercent > 0 ? "up" : "down");
}

function updateStats(price, history) {

    const combined = Array.isArray(history) && history.length > 0
        ? [...history, price]
        : [price, ...sessionHistory];

    const high = Math.max(...combined);
    const low = Math.min(...combined);

    setText("statHigh", high.toLocaleString("en-IN", { maximumFractionDigits: 2 }));
    setText("statLow", low.toLocaleString("en-IN", { maximumFractionDigits: 2 }));
    setText("statPoints", sessionHistory.length + 1);
}

function pushToChart(price) {

    sessionLabels.push(new Date().toLocaleTimeString());
    sessionHistory.push(price);

    if (sessionLabels.length > DETAIL_MAX_POINTS) {
        sessionLabels.shift();
        sessionHistory.shift();
    }

    if (detailChart) {
        detailChart.update();
    }
}

function showLiveBadge(isLive) {
    const el = document.getElementById("liveBadge");
    if (!el) return;
    el.textContent = isLive ? "● LIVE" : "● USING LAST KNOWN VALUE";
    el.className = "live-badge " + (isLive ? "is-live" : "is-stale");
}

function setText(id, text) {
    const el = document.getElementById(id);
    if (el) el.innerHTML = text;
}

function hexToRgba(hex, alpha) {
    const r = parseInt(hex.slice(1, 3), 16);
    const g = parseInt(hex.slice(3, 5), 16);
    const b = parseInt(hex.slice(5, 7), 16);
    return `rgba(${r},${g},${b},${alpha})`;
}

initDetailPage();
