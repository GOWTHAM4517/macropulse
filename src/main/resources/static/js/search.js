/* =========================================================
   MacroPulse — Homepage navigation search
   Filters the "where do you want to go" nav cards as the
   user types, shows a small status line, and adds a clear
   button.
========================================================= */

function searchCards() {

    const input = document.getElementById("searchBox");
    const filter = input.value.trim().toUpperCase();
    const cards = document.querySelectorAll(".nav-card");
    const status = document.getElementById("searchStatus");

    let matchCount = 0;

    cards.forEach(card => {

        const text = card.innerText.toUpperCase();
        const matches = filter === "" || text.includes(filter);

        card.classList.toggle("search-hidden", !matches);
        card.classList.toggle("search-match", matches && filter !== "");

        if (matches) matchCount++;
    });

    if (!status) return;

    if (filter === "") {
        status.textContent = "";
    } else if (matchCount === 0) {
        status.textContent = `No results for "${input.value}"`;
    } else {
        status.textContent = `${matchCount} result${matchCount === 1 ? "" : "s"} for "${input.value}"`;
    }
}

function clearSearch() {

    const input = document.getElementById("searchBox");
    input.value = "";
    searchCards();
    input.focus();
}

function wireSearchClear() {

    const clearBtn = document.getElementById("searchClear");
    if (clearBtn) {
        clearBtn.addEventListener("click", clearSearch);
    }
}

wireSearchClear();
