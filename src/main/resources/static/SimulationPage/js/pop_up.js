import {toggleStartStop, startSimulation} from "./simulation.js";

document.addEventListener('DOMContentLoaded', () => {
    const popupOverlay = document.getElementById('popup-overlay');
    const cancelBtn = document.getElementById('cancel-btn');

    document.getElementById('start-button').addEventListener('click', showPopup);

    cancelBtn.addEventListener('click', closePopup);
    popupOverlay.addEventListener('click', closePopup);
});

function check_if_scenario_exists(){
    let name = document.getElementById("scenario-name")

    return fetch("/api/v1/scenario/name/" + name.value, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        }
    })
}

function updateSearchDisplay(){
    const confirmBtn = document.getElementById('confirm-btn');
    let search_display = document.getElementById("search_display")
    check_if_scenario_exists()
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return response.json();
        })
        .then((data) => {
            console.log("Successfully found scenario:", data);

            search_display.innerText = "Scenario found!"
            search_display.style.color = "green"

            let scenario_name = document.getElementById("scenario-name").value

            confirmBtn.disabled = false
            confirmBtn.addEventListener("click", () => startSimulation(scenario_name))
            confirmBtn.addEventListener("click", toggleStartStop)
            confirmBtn.addEventListener("click", closePopup)

        })
        .catch((error) => {
            console.error("Error finding scenario:", error);

            search_display.innerText = "Scenario not found."
            search_display.style.color = "red"
        });
}

function closePopup() {
    const popupOverlay = document.getElementById('popup-overlay');
    const popup = document.getElementById('popup');

    popupOverlay.style.display = 'none';
    popup.style.display = 'none';
}

export function showPopup() {
    const popupData = {
        title: "Enter Scenario Name",
        content: `
            <div class="popup-body" id="popup-content">
                <div class="popup-container">
                
                    <div class="popup-section">

                        <label>Name<br>
                            <input type="text" id="scenario-name" placeholder="Enter Name of Scenario">
                            <button id="search-button">&#x1F50E;</button>
                        </label>
                        
                        <br>
                        
                        <span id="search_display"></span>
                        
                        <br>
                    </div>
                    
                </div>
            </div>`
    }

    const popupOverlay = document.getElementById('popup-overlay');
    const popup = document.getElementById('popup');
    const popupTitle = document.getElementById('popup-title');
    const popupContent = document.getElementById('popup-content');
    const confirmBtn = document.getElementById('confirm-btn');

    const data = popupData;
    popupTitle.innerText = data.title;
    popupContent.innerHTML = data.content;

    document.getElementById('search-button').addEventListener('click', updateSearchDisplay);
    confirmBtn.disabled = true

    popupOverlay.style.display = 'block';
    popup.style.display = 'block';
}