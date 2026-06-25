/* =========================================================
   MacroPulse — Homepage hero stats
   Fills in the four live numbers under the homepage hero
   (Bitcoin, Gold, USD, Nifty). Separate from nav-ticker.js
   since only the homepage has this hero block.
========================================================= */

const HERO_POLL_MS = 30000;

const HERO_TARGETS = {
    bitcoin: "heroBitcoin",
    gold: "heroGold",
    usd: "heroUsd",
    nifty: "heroNifty"
};

async function refreshHeroStats() {

    for (const key of Object.keys(HERO_TARGETS)) {

        const asset = ASSETS[key];
        if (!asset) continue;

        try {

            const data = await fetch(asset.endpoint).then(r => r.json());
            const price = data[asset.priceField];
            const formatted = formatAssetPrice(asset, price);

            const el = document.getElementById(HERO_TARGETS[key]);
            if (el) el.textContent = formatted;

        } catch (error) {
            console.log(error);
        }
    }
}

refreshHeroStats();
setInterval(refreshHeroStats, HERO_POLL_MS);
