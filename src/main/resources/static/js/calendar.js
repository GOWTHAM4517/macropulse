/* =========================================================
   MacroPulse — Economic Calendar Page
   Loads the sample economic calendar events from the backend
   and renders them into the table.
========================================================= */

async function loadCalendar() {

    try {

        const events = await fetch("/economic-calendar").then(r => r.json());

        const calendarHtml = events.map(event => `
            <tr>
                <td>${event.time}</td>
                <td>${event.country}</td>
                <td>${event.event}</td>
            </tr>
        `).join("");

        document.querySelector("#calendarTable tbody").innerHTML = calendarHtml;

    } catch (error) {
        console.log(error);
    }
}

loadCalendar();
