/* =========================================================
   MacroPulse — Nav ticker
   Drives the live ticker strip in the sticky header. Included
   on every page. Reuses the shared ASSETS registry so the
   ticker never drifts from the rest of the site.
========================================================= */

const TICKER_POLL_MS = 30000;
const TICKER_KEYS = ["bitcoin", "gold", "usd", "nifty"];

async function refreshTicker() {

    for (const key of TICKER_KEYS) {

        const asset = ASSETS[key];
        if (!asset) continue;

        try {

            const data = await fetch(asset.endpoint).then(r => r.json());
            const price = data[asset.priceField];
            const formatted = formatAssetPrice(asset, price);

            document.querySelectorAll(`[data-ticker="${key}"] strong`).forEach(el => {
                el.textContent = formatted;
            });

        } catch (error) {
            console.log(error);
        }
    }
}

function wireNavShrink() {

    const nav = document.getElementById("siteNav");
    if (!nav) return;

    window.addEventListener("scroll", () => {
        nav.classList.toggle("scrolled", window.scrollY > 40);
    });
}

function markActiveNavLink() {

    const currentPage = window.location.pathname.split("/").pop() || "index.html";

    document.querySelectorAll(".nav-links a").forEach(link => {
        const linkPage = link.getAttribute("href");
        if (linkPage === currentPage) {
            link.classList.add("active");
        }
    });
}

wireNavShrink();
markActiveNavLink();
refreshTicker();
setInterval(refreshTicker, TICKER_POLL_MS);
