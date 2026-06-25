/* =========================================================
   MacroPulse — Asset Registry
   Single source of truth for every live asset. Each key here
   has a matching <key>.html page. Add a new asset here plus
   one new HTML page (copy an existing one and change
   ASSET_KEY), and the nav ticker, homepage cards, and search
   all pick it up automatically.
========================================================= */

const ASSETS = {

    gold: {
        title: "Gold",
        subtitle: "24K Gold · per gram",
        endpoint: "/gold",
        priceField: "price",
        currencySymbol: "₹",
        color: "#FFD700",
        page: "gold.html",
        icon: "🥇"
    },

    silver: {
        title: "Silver",
        subtitle: "Silver · per gram",
        endpoint: "/silver",
        priceField: "price",
        currencySymbol: "₹",
        color: "#C0C0C0",
        page: "silver.html",
        icon: "🥈"
    },

    usd: {
        title: "USD / INR",
        subtitle: "US Dollar exchange rate",
        endpoint: "/usd",
        priceField: "usdToInr",
        currencySymbol: "₹",
        color: "#00d4ff",
        page: "usd.html",
        icon: "💵"
    },

    bitcoin: {
        title: "Bitcoin",
        subtitle: "BTC · world's largest cryptocurrency",
        endpoint: "/bitcoin",
        priceField: "price",
        currencySymbol: "₹",
        color: "#f7931a",
        page: "bitcoin.html",
        icon: "₿"
    },

    ethereum: {
        title: "Ethereum",
        subtitle: "ETH · smart-contract platform",
        endpoint: "/ethereum",
        priceField: "priceInr",
        currencySymbol: "₹",
        color: "#9b8cff",
        page: "ethereum.html",
        icon: "◆"
    },

    nifty: {
        title: "Nifty 50",
        subtitle: "NSE benchmark index",
        endpoint: "/nifty",
        priceField: "price",
        currencySymbol: "",
        color: "#00FF7F",
        page: "nifty.html",
        icon: "📈"
    }

};

function formatAssetPrice(asset, value) {
    if (typeof value !== "number") return "Loading...";
    const decimals = value > 1000 ? 0 : 2;
    return asset.currencySymbol + Number(value).toLocaleString("en-IN", {
        maximumFractionDigits: decimals
    });
}
