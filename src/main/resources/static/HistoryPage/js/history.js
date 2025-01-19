const mockScenarios = [
    {
        name: "Scenario 1",
        startDate: new Date("2023-01-01T10:00:00"),
        endDate: new Date("2023-01-01T14:00:00"),
        duration: { hours: 4, minutes: 0, seconds: 0 },
        assetNames: ["Solar Panel", "Wind Turbine"],
        records: [],
    },
    {
        name: "Scenario 2",
        startDate: new Date("2023-02-01T08:00:00"),
        endDate: new Date("2023-02-01T12:00:00"),
        duration: { hours: 4, minutes: 0, seconds: 0 },
        assetNames: ["Fuel Cell", "Accumulator"],
        records: [],
    },
    {
        name: "Scenario 3",
        startDate: new Date("2023-03-01T09:00:00"),
        endDate: new Date("2023-03-01T17:00:00"),
        duration: { hours: 8, minutes: 0, seconds: 0 },
        assetNames: ["Generic Consumer"],
        records: [],
    },
];

function handleScrollAnimation() {
    const elements = document.querySelectorAll('.animate-slide-in-left');
    elements.forEach(element => {
        const position = element.getBoundingClientRect();
        if (position.top < window.innerHeight && position.bottom >= 0) {
            element.classList.add('show');
        }
    });
}

window.addEventListener('scroll', handleScrollAnimation);
window.addEventListener('load', handleScrollAnimation);

function groupScenarios(data) {
    const scenarioMap = {};

    data.forEach(entry => {
        const scenarioName = entry.simulation.scenario.name;
        const dateString = entry.environmental_changes.date;
        const date = new Date(dateString);

        // Initialize array if first time seeing this scenario
        if (!scenarioMap[scenarioName]) {
            scenarioMap[scenarioName] = {
                name: scenarioName,
                records: []
            };
        }

        scenarioMap[scenarioName].records.push({
            date,
            assetsResults: entry.assets_results,
            scenario: entry.simulation.scenario
        });
    });

    // Convert map to array of scenario objects
    const scenarios = Object.values(scenarioMap).map(scenarioObj => {
        const { name, records } = scenarioObj;
        // Sort records by ascending date
        records.sort((a, b) => a.date - b.date);

        // Start & End times
        const startDate = records[0].date;
        const endDate   = records[records.length - 1].date;
        const durationMs = endDate - startDate;
        const totalSec = Math.floor(durationMs / 1000);
        const hours   = Math.floor(totalSec / 3600);
        const minutes = Math.floor((totalSec % 3600) / 60);
        const seconds = totalSec % 60;

        // Gather all unique assets from `assetsResults`
        const assetNameSet = new Set();
        records.forEach(r => {
            if (r.assetsResults) {
                Object.keys(r.assetsResults).forEach(assetKey => {
                    assetNameSet.add(assetKey);
                });
            }
        });

        return {
            name,
            startDate,
            endDate,
            duration: { hours, minutes, seconds },
            assetNames: Array.from(assetNameSet),
            records
        };
    });

    return scenarios;
}

function renderScenarios(scenarios) {
    const container = document.getElementById('cards-container');
    container.innerHTML = ''; // Clear any existing content

    scenarios.forEach(scenario => {
        const card = createScenarioCard(scenario);
        container.appendChild(card);
    });
}

function createScenarioCard(scenario) {
    const cardDiv = document.createElement('div');
    cardDiv.className = 'session-card animate-slide-in-left';

    // Name which can be used by delete button.
    cardDiv.setAttribute('data-scenario-name', scenario.name);

    // Header
    const headerDiv = document.createElement('div');
    headerDiv.className = 'card-header';

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.className = 'card-checkbox';

    const h3 = document.createElement('h3');
    h3.textContent = scenario.name;

    headerDiv.appendChild(checkbox);
    headerDiv.appendChild(h3);

    // Content
    const contentDiv = document.createElement('div');
    contentDiv.className = 'card-content';

    // Dates
    const dateTimeDiv = document.createElement('div');
    dateTimeDiv.className = 'date-time';

    const pStart = document.createElement('p');
    pStart.innerHTML = `<strong>Start: </strong>${scenario.startDate.toLocaleString()}`;

    const pEnd = document.createElement('p');
    pEnd.innerHTML = `<strong>End: </strong>${scenario.endDate.toLocaleString()}`;

    dateTimeDiv.appendChild(pStart);
    dateTimeDiv.appendChild(pEnd);

    // Duration
    const durationDiv = document.createElement('div');
    durationDiv.className = 'duration';

    const { hours, minutes, seconds } = scenario.duration;
    const pDuration = document.createElement('p');
    pDuration.innerHTML = `<strong>Duration: </strong>${hours}h ${minutes}min ${seconds}s`;
    durationDiv.appendChild(pDuration);

    // Assets
    const assetDiv = document.createElement('div');
    assetDiv.className = 'asset';

    const pAssets = document.createElement('p');
    pAssets.innerHTML = `<strong>Assets simulated: </strong>${scenario.assetNames.join(', ')}`;
    assetDiv.appendChild(pAssets);

    // Outcome (just a placeholder for now)
    const outcomeDiv = document.createElement('div');
    outcomeDiv.className = 'outcome';

    const pOutcome = document.createElement('p');
    pOutcome.innerHTML = `<strong style="color: green">Success</strong>`;
    outcomeDiv.appendChild(pOutcome);

    contentDiv.appendChild(dateTimeDiv);
    contentDiv.appendChild(durationDiv);
    contentDiv.appendChild(assetDiv);
    contentDiv.appendChild(outcomeDiv);

    // Combine header + content
    cardDiv.appendChild(headerDiv);
    cardDiv.appendChild(contentDiv);

    // Add click handler to show modal
    contentDiv.addEventListener('click', () => {
        showModal(scenario);
    });

    return cardDiv;
}

function showModal(scenario) {
    const overlay  = document.getElementById('modal-overlay');
    const modalDiv = document.getElementById('modal-content');

    // Clear old content
    modalDiv.innerHTML = '';

    // Title
    const title = document.createElement('h2');
    title.textContent = `${scenario.name} - Detailed Data`;
    modalDiv.appendChild(title);

    // For each record, show date + a table of assets_results
    scenario.records.forEach((rec, idx) => {
        const recordTitle = document.createElement('h4');
        recordTitle.textContent = `Timestamp: ${rec.date.toLocaleString()}`;
        modalDiv.appendChild(recordTitle);

        const table = document.createElement('table');
        const thead = document.createElement('thead');
        thead.innerHTML = `
      <tr>
        <th>Asset</th>
        <th>Value</th>
      </tr>
    `;
        table.appendChild(thead);

        const tbody = document.createElement('tbody');
        if (rec.assetsResults) {
            Object.entries(rec.assetsResults).forEach(([asset, value]) => {
                const row = document.createElement('tr');
                row.innerHTML = `
          <td>${asset}</td>
          <td>${value}</td>
        `;
                tbody.appendChild(row);
            });
        }
        table.appendChild(tbody);
        modalDiv.appendChild(table);

        // Separator
        if (idx < scenario.records.length - 1) {
            const hr = document.createElement('hr');
            modalDiv.appendChild(hr);
        }
    });

    // Close button
    const closeButton = document.createElement('button');
    closeButton.textContent = 'Close';
    closeButton.addEventListener('click', closeModal);
    modalDiv.appendChild(closeButton);

    // Show overlay
    overlay.style.display = 'flex';
}

function closeModal() {
    document.getElementById('modal-overlay').style.display = 'none';
}

function requestSimulationStatus() {
    return fetch("/api/v1/simulations_status", {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
}
window.addEventListener('DOMContentLoaded', () => {
    requestSimulationStatus()
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return response.json();
        })
        .then((data) => {
            console.log("Successfully found scenario:", data);

            const scenarios = groupScenarios(data);
            renderScenarios(mockScenarios);

            select_deselect_all_cards();
        })
        .catch((error) => {
            console.error("Error finding scenario:", error);
        });

    function select_deselect_all_cards(){
        let selectAllCheckbox = document.getElementById("select-all");
        let cardCheckboxes = document.querySelectorAll('.card-header input[type="checkbox"]');

        // Select/deselect all sessions
        selectAllCheckbox.addEventListener('change', function () {
            const isChecked = selectAllCheckbox.checked;
            cardCheckboxes.forEach(checkbox => {
                checkbox.checked = isChecked;
            });
        });

        // Synchronization of the "select all" state when single sessions are selected
        cardCheckboxes.forEach(checkbox => {
            checkbox.addEventListener("change", () => {
                const allChecked = [...cardCheckboxes].every(checkbox => checkbox.checked);
                selectAllCheckbox.checked = allChecked;
            });
        });
    }

    function getCheckedScenarioNames() {
        const cards = document.querySelectorAll('.session-card');
        const checkedScenarios = [];

        cards.forEach(card => {
            const checkbox = card.querySelector('.card-checkbox');
            if (checkbox && checkbox.checked) {
                const scenarioName = card.getAttribute('data-scenario-name');
                checkedScenarios.push(scenarioName);
            }
        });

        return checkedScenarios;
    }

    function deleteScenarios(){
        let scenarios = getCheckedScenarioNames();

        console.log("checkedNames")
        console.log(scenarios)
        fetch("/api/v1/simulations_status", {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(scenarios)
        }).then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

        })
            .then(() => {
                console.log("Successfully deleted scenarios");
                window.location.reload();
            })
            .catch((error) => {
                console.error("Error deleting scenarios:", error);
                window.location.reload();
            });
    }

    document.getElementById("delete-selected").addEventListener("click", deleteScenarios);
});
