import {createNode, saveTopology, loadCanvasState} from './assets.js';

// Used to show the chosen name in the preview at the center of the save popup
function updatePreview() {
    const nameInput = document.getElementById("name");
    const previewText = document.getElementById("preview-node");
    previewText.textContent = nameInput.value || "Your Preview";
}

window.updatePreview = updatePreview;

document.addEventListener('DOMContentLoaded', () => {
    const popupOverlay = document.getElementById('popup-overlay');
    const popup = document.getElementById('popup');
    const popupTitle = document.getElementById('popup-title');
    const popupContent = document.getElementById('popup-content');
    const confirmBtn = document.getElementById('confirm-btn');
    const cancelBtn = document.getElementById('cancel-btn');

    const popupData = {
        add: {
            title: "Add New Asset",
            content: `
                <div class="popup-body" id="popup-content">
                    <div class="popup-container">
                    
                        <div class="popup-section">
                            <h4>Generic Data</h4>
                            
                            <label>Name<br>
                                <input type="text" id="name" placeholder="Enter name" oninput="updatePreview()">
                            </label><br>
                            
                            <label>Type<br>
                                <select id="asset-type-select">
                                </select>
                            </label><br>
                            
                            <label>Role<br>
                                <div class="radio-group">
                                    <label>
                                        <input type="radio" name="role" value="Consumer" id="type-consumer"> 
                                        <span class="radio-label">Consumer</span>
                                    </label>
                                    <label>
                                        <input type="radio" name="role" value="Producer" id="type-producer" checked> 
                                        <span class="radio-label">Producer</span>
                                    </label>
                                </div>
                            </label>
                            
                        </div>

                        <div class="popup-preview">
                            <span id="preview-node" class="node">Preview</span>
                        </div>

                        <div id="dynamic-inputs" class="popup-section">
                            <!-- Dynamic inputs will be rendered here -->
                        </div>
                    </div>
                </div>`
        },
        delete: {
            title: "Delete Scenario",
            content: `
                <p>Name</p>
                <input type="text" placeholder="Scenario Name" id="scenario-name">
                <button id="search-button-load">&#x1F50E;</button>` // Search symbol
        },
        load: {
            title: "Load Scenario",
            content: `<p>Name</p>
                      <input type="text" placeholder="Scenario Name" id="scenario-name">
                      <button id="search-button-load">&#x1F50E;</button>` // Search symbol
        },
        save: {
            title: "Save Scenario",
            content: `<p>Name</p>
                      <input type="text" placeholder="Scenario Name" id="scenario-name">`
        }
    };

    const optionsConfig = {
        solar_panel: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power" placeholder="Enter nominal power" name="asset-info">
            </label><br>
            
            <label>Area (m²)<br>
                <input type="number" id="area" placeholder="Enter area" name="asset-info">
            </label><br>
            
            <label>Temperature (°C)<br>
                <input type="number" id="temperature" placeholder="Enter temperature" name="asset-info">
            </label><br>
            
            <label>Efficiency (%)<br>
                <input type="number" id="efficiency" placeholder="Enter efficiency" name="asset-info">
            </label><br>
        `,
        accumulator: `
            <label>Capacity (A/h)<br>
                <input type="number" id="capacity" placeholder="Enter capacity" name="asset-info">
            </label><br>
            
            <label>Nominal Voltage (Wh)<br>
                <input type="number" id="nominal-voltage" placeholder="Enter nominal voltage" name="asset-info">
            </label><br>
            
            <label>Current Charge (A/h)<br>
                <input type="number" id="current-charge" placeholder="Enter current charge" name="asset-info">
            </label><br>
        `,
        wind_turbine: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power" placeholder="Enter nominal power" name="asset-info">
            </label><br>
            
            <label>Blade Length (m)<br>
                <input type="number" id="blade-length" placeholder="Enter blade length" name="asset-info">
            </label><br>
            
            <label>Dissipation Factor<br>
                <input type="number" id="dissipation-factor" placeholder="Enter dissipation factor" name="asset-info">
            </label><br>
        `,
        fuel_cell: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power" placeholder="Enter nominal power" name="asset-info">
            </label><br>
            
            <label>Fuel Capacity (l)<br>
                <input type="number" id="fuel-capacity" placeholder="Enter fuel capacity" name="asset-info">
            </label><br>
            
            <label>Current Fuel (l)<br>
                <input type="number" id="current-fuel" placeholder="Enter current fuel" name="asset-info">
            </label><br>
        `,
        generic_consumer: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power" placeholder="Enter nominal power" name="asset-info">
            </label><br>
            
            <label>Min Consumption (W)<br>
                <input type="number" id="min-consumption" placeholder="Enter min consumption" name="asset-info">
            </label><br>
        `
    };

    function updateDynamicInputs(selectedType) {
        const dynamicInputsContainer = document.getElementById("dynamic-inputs");
        if (!dynamicInputsContainer) return;

        // Update the inputs based on the selected type
        dynamicInputsContainer.innerHTML = optionsConfig[selectedType] || "";
    }

    // Define the options for Producer and Consumer
    const producerOptions = [
        {value: "solar_panel", text: "Solar Panel"},
        {value: "fuel_cell", text: "Fuel Cell"},
        {value: "wind_turbine", text: "Wind Turbine"}
    ];

    const consumerOptions = [
        {value: "accumulator", text: "Accumulator"},
        {value: "generic_consumer", text: "Generic Consumer"}
    ];

    function updateSelectOptions(selectedType) {
        const selectElement = document.getElementById("asset-type-select");
        if (!selectElement) return;

        const preview = document.getElementById("preview-node")

        // Clear existing options
        selectElement.innerHTML = "";

        // Determine which options to display
        const optionsToShow =
            selectedType === "Producer" ? producerOptions : consumerOptions;

        if (selectedType === "Producer") {
            preview.style = "border-left: 1rem solid #F98491; position:static"
            selectElement.value = "solar_panel";
            updateDynamicInputs("solar_panel");
        } else {
            preview.style = "border-left: 1rem solid #597445; position:static"
            selectElement.value = "accumulator";
            updateDynamicInputs("accumulator");
        }

        // Populate the select element
        optionsToShow.forEach(option => {
            const opt = document.createElement("option");
            opt.value = option.value;
            opt.textContent = option.text;
            selectElement.appendChild(opt);
        });

        // Add change event listener to update inputs dynamically
        selectElement.addEventListener("change", () => {
            updateDynamicInputs(selectElement.value);
        });
    }

    function showPopup(type) {
        const data = popupData[type];
        popupTitle.innerText = data.title;
        popupContent.innerHTML = data.content;

        if (type === "add") {
            const radioButtons = document.getElementsByName("role");
            updateSelectOptions("Producer"); // Initialize with default options

            radioButtons.forEach(radio => {
                radio.addEventListener("change", () => {
                    updateSelectOptions(radio.value);
                });
            });

            confirmBtn.addEventListener("click", add_listener);
        }

        if (type === "save") {
            confirmBtn.addEventListener("click", save_listener);
        }

        if(type === "load"){
            document.getElementById("search-button-load").addEventListener("click", check_if_scenario_exists)
            confirmBtn.addEventListener("click", load_listener);
        }

        if(type === "delete"){
            document.getElementById("search-button-load").addEventListener("click", check_if_scenario_exists)
            confirmBtn.addEventListener("click", delete_listener);
        }

        popupOverlay.style.display = 'block';
        popup.style.display = 'block';
    }

    function closePopup() {
        popupOverlay.style.display = 'none';
        popup.style.display = 'none';
    }

    document.getElementById('add').addEventListener('click', () => showPopup('add'));
    document.getElementById('delete').addEventListener('click', () => showPopup('delete'));
    document.getElementById('load').addEventListener('click', () => showPopup('load'));
    document.getElementById('save').addEventListener('click', () => showPopup('save'));

    cancelBtn.addEventListener('click', closePopup);
    popupOverlay.addEventListener('click', closePopup);

    // Add new node functionality
    function add_listener() {
        const name = document.getElementById("name").value;
        const inputs = document.getElementsByName("asset-info");
        const role = document.querySelector('input[name="role"]:checked').value;

        const type = document.getElementById("asset-type-select").value
        let asset_info = {}

        inputs.forEach(input => {
            asset_info[input.id] = input.value;
        });

        const asset = {
            configuration: asset_info,
            type: type,
            name: name,
            role: role
        };

        fetch("/api/v1/asset", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(asset),
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                console.log("Successfully saved asset:", data);
                const newId = `node${Date.now()}`;
                const newNode = createNode(newId, name, 300, 300, "custom created node");
                jsPlumb.repaintEverything();
                closePopup();
            })
            .catch((error) => {
                console.error("Error saving asset:", error);
                alert("Failed to save asset. Try to use another name");
            });
    }

    function save_listener(){
        saveTopology()
            .then((data) => {
            console.log("Topology saved successfully!", data);
                closePopup();
            })
            .catch((error) => {
                console.log("Error occurred while saving topology:", error.message);
            })
            .finally(() => {
                console.log("Save topology process completed");
            });
    }

    function load_listener(){
        let name = document.getElementById("scenario-name")

        fetch("/api/v1/scenario/name/" + name.value, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                return response.json();
            })
            .then((data) => {
                loadCanvasState(data);
                closePopup();
                console.log("Successfully load canvas state:", data);
                alert("Canvas state load successfully!");
            })
            .catch((error) => {
                console.error("Error loading canvas state:", error);
                alert("Failed to load canvas state.");
            });
    }

    function delete_listener(){
        let name = document.getElementById("scenario-name")

        fetch("/api/v1/scenario/name/" + name.value, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                return response.json();
            })
            .then((data) => {
                console.log("Successfully deleted scenario:", data);
                closePopup();
            })
            .catch((error) => {
                console.error("Error deleting scenario:", error);
            });
    }

    function check_if_scenario_exists(){
        let name = document.getElementById("scenario-name")

        fetch("/api/v1/scenario/name/" + name.value, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            }
        })
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                return response.json();
            })
            .then((data) => {
                console.log("Successfully found scenario:", data);
            })
            .catch((error) => {
                console.error("Error finding scenario:", error);
            });
    }
});