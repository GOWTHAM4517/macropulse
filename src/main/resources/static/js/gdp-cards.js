/* =========================================================
   MacroPulse — GDP snapshot cards
   Populates the India/USA/China GDP figures shown at the top
   of the GDP Race page.
========================================================= */

async function loadGdpCards() {

    try {

        const countries = await fetch("/countries").then(r => r.json());

        const india = countries.find(c => c.country === "India");
        const usa = countries.find(c => c.country === "USA");
        const china = countries.find(c => c.country === "China");

        if (india) setText("indiaGDP", "$" + india.gdpTrillionUsd + "T");
        if (usa) setText("usaGDP", "$" + usa.gdpTrillionUsd + "T");
        if (china) setText("chinaGDP", "$" + china.gdpTrillionUsd + "T");

    } catch (error) {
        console.log(error);
    }
}

function setText(id, text) {
    const el = document.getElementById(id);
    if (el) el.innerHTML = text;
}

loadGdpCards();
