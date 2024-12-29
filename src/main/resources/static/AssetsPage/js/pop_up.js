import {createNode, saveTopology} from './assets.js';

// Used to show the chosen name in the preview at the center of the save popup
function updatePreview() {
    const nameInput = document.getElementById("name-input");
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
                                <input type="text" id="name-input" placeholder="Enter name" oninput="updatePreview()">
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
                <input type="text" placeholder="Scenario Name">
                <button>&#x1F50E;</button>` // Search symbol
        },
        load: {
            title: "Load Scenario",
            content: `<p>Name</p>
                      <input type="text" placeholder="Scenario Name">
                      <button>&#x1F50E;</button>` // Search symbol
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
                <input type="number" id="nominal-power-input" placeholder="Enter nominal power">
            </label><br>
            
            <label>Area (m²)<br>
                <input type="number" id="area-input" placeholder="Enter area">
            </label><br>
            
            <label>Temperature (°C)<br>
                <input type="number" id="temperature-input" placeholder="Enter temperature">
            </label><br>
            
            <label>Efficiency (%)<br>
                <input type="number" id="efficiency-input" placeholder="Enter efficiency">
            </label><br>
        `,
        accumulator: `
            <label>Capacity (A/h)<br>
                <input type="number" id="capacity-input" placeholder="Enter capacity">
            </label><br>
            
            <label>Nominal Voltage (Wh)<br>
                <input type="number" id="nominal-voltage-input" placeholder="Enter nominal voltage">
            </label><br>
            
            <label>Current Charge (A/h)<br>
                <input type="number" id="current-charge-input" placeholder="Enter current charge">
            </label><br>
        `,
        wind_turbine: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power-input" placeholder="Enter nominal power">
            </label><br>
            
            <label>Blade Length (m)<br>
                <input type="number" id="blade-length-input" placeholder="Enter blade length">
            </label><br>
            
            <label>Dissipation Factor<br>
                <input type="number" id="dissipation-factor-input" placeholder="Enter dissipation factor">
            </label><br>
        `,
        fuel_cell: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power-input" placeholder="Enter nominal power">
            </label><br>
            
            <label>Fuel Capacity (l)<br>
                <input type="number" id="fuel-capacity-input" placeholder="Enter fuel capacity">
            </label><br>
            
            <label>Current Fuel (l)<br>
                <input type="number" id="current-fuel-input" placeholder="Enter current fuel">
            </label><br>
        `,
        generic_consumer: `
            <label>Nominal Power (W)<br>
                <input type="number" id="nominal-power-input" placeholder="Enter nominal power">
            </label><br>
            
            <label>Min Consumption (W)<br>
                <input type="number" id="min-consumption-input" placeholder="Enter min consumption">
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

        if (type === "save"){
            confirmBtn.addEventListener("click", saveTopology);
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
    function add_listener(){
        let name = document.getElementById("name-input").value;
        const newId = `node${Date.now()}`;
        const newNode = createNode(newId, name, 300, 300);
        jsPlumb.repaintEverything();
        closePopup();
    }
});