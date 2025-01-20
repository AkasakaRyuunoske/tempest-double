import {showPopup} from "./pop_up.js";

let updateInterval = null;
let scenario_name = null;
let assetsProducers = [];
const startButton = document.querySelector(".start-button button");
let assetColors = {};

const maxCapacities = {
    satisfaction: 100,
    production: 200,
    accumulator: 150,
    washingMachine: 5,
    refrigerator: 2,
    airConditioner: 3,
    solarPanel: 10,
    windTurbine: 15,
    accumulatorBar: 20
};

let productionData = [];
let xValue = 0;

const hexToRgb = hex => {
    const bigint = parseInt(hex.replace(/^#/, ''), 16);
    return { r: (bigint >> 16) & 255, g: (bigint >> 8) & 255, b: bigint & 255 };
};

const rgbToHex = (r, g, b) =>
    `#${[r, g, b].map(c => Math.min(255, Math.max(0, Math.round(c))).toString(16).padStart(2, '0')).join('')}`;

const generateColorNearBase = (baseColor, range = 40) => {
    const { r, g, b } = hexToRgb(baseColor);

    const rShift = Math.random() * range * 2 - range;
    const gShift = Math.random() * range * 2 - range;
    const bShift = Math.random() * range * 2 - range;

    const newR = Math.max(0, Math.min(255, r + rShift));
    const newG = Math.max(0, Math.min(255, g + gShift));
    const newB = Math.max(0, Math.min(255, b + bShift));

    return rgbToHex(newR, newG, newB);
};

const washingMachineColor = generateColorNearBase("#FF8A8A");
const refrigeratorColor = generateColorNearBase("#FF8A8A");
const airConditionerColor = generateColorNearBase("#FF8A8A");

const consumptionGraph = new CanvasJS.Chart("consumptionGraph", {
    title: { text: "Consumption", fontSize: 25, fontFamily: "Verdana" },
    axisX: { gridThickness: 1, gridDashType: "solid" },
    axisY: { title: "W", includeZero: false, gridThickness: 1, gridDashType: "solid" },
    data: []
});

const productionGraph = new CanvasJS.Chart("productionGraph", {
    title: { text: "Production", fontSize: 25, fontFamily: "Verdana" },
    axisX: { gridThickness: 1, gridDashType: "solid" },
    axisY: { title: "W", includeZero: false, gridThickness: 1, gridDashType: "solid" },
    data: [
        {
            type: "line",
            color: "#597445",
            name: "Total Production",
            showInLegend: true,
            dataPoints: productionData,
        }
    ]
});

function setProgressBarColors() {
    document.getElementById("washingMachineBar").style.backgroundColor = washingMachineColor;
    document.getElementById("refrigeratorBar").style.backgroundColor = refrigeratorColor;
    document.getElementById("airConditionerBar").style.backgroundColor = airConditionerColor;
}

function updateProgressBar(id, value, maxCapacity, valueDisplayId) {
    const bar = document.getElementById(id);
    const valueDisplay = document.getElementById(valueDisplayId);
    const percentage = (value / maxCapacity) * 100;

    bar.style.width = percentage + "%";
    bar.textContent = "";

    if (valueDisplay) {
        valueDisplay.textContent = value.toFixed(2) + " W";
    }
}

function updateWValues(currentId, maxId, currentValue, maxValue) {
    document.getElementById(currentId).textContent = currentValue.toFixed(2);
    document.getElementById(maxId).textContent = maxValue.toFixed(2);
}

function updateDashboard(data) {
    fetch("/api/v1/simulation", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
        .then((data) => {
            console.log(data)

            Object.entries(data).forEach(([name, value]) => {
                // Handle specific cases for total energy consumed and produced
                if (name === "total_energy_consumed") {
                    document.getElementById("current-satisfaction").innerText = value.toFixed(2);
                    let max_value = document.getElementById("max-satisfaction").innerText
                    updateProgressBar(`satisfactionBar`, value, Number(max_value), `${name}-value-display`);
                    return; // skip
                }

                if (name === "total_energy_produced") {
                    document.getElementById("current-production").innerText = value.toFixed(2);
                    let max_value = document.getElementById("max-production").innerText
                    updateProgressBar(`productionBar`, value, Number(max_value), `${name}-value-display`);
                    return; // skip
                }

                if (assetsProducers.includes(name)){
                    // Handle dynamic devices
                    let deviceIndex = productionGraph.options.data.findIndex(
                        (line) => line.name === name
                    );

                    if (deviceIndex !== -1) {
                        // If the device already exists in the graph, update its dataPoints
                        productionGraph.options.data[deviceIndex].dataPoints.push({
                            x: xValue,
                            y: value,
                        });
                    } else {
                        // If the device is new, dynamically add it to the graph
                        productionGraph.options.data.push({
                            type: "line",
                            color: assetColors[name],
                            name: name,
                            showInLegend: true,
                            dataPoints: [{ x: xValue, y: value }],
                        });
                    }
                } else {
                    // Handle dynamic devices
                    let deviceIndex = consumptionGraph.options.data.findIndex(
                        (line) => line.name === name
                    );

                    if (deviceIndex !== -1) {
                        // If the device already exists in the graph, update its dataPoints
                        consumptionGraph.options.data[deviceIndex].dataPoints.push({
                            x: xValue,
                            y: value,
                        });
                    } else {
                        // If the device is new, dynamically add it to the graph
                        consumptionGraph.options.data.push({
                            type: "line",
                            color: assetColors[name],
                            name: name,
                            showInLegend: true,
                            dataPoints: [{ x: xValue, y: value }],
                        });
                    }
                }

                let max_value = document.getElementById(`${name}-max-value`).innerText
                console.log("Max value for " + name + " is => " + max_value)
                updateProgressBar(`${name}-progress-bar`, value, Number(max_value), `${name}-value-display`);
            });
        })

        .catch((error) => {
            console.error("Error starting simulation:", error);
            alert("Failed to start simulation.");

            const startButton = document.querySelector(".start-button button");

            startButton.textContent = "Start";
            startButton.classList.remove("stop");
            if (updateInterval) {
                clearInterval(updateInterval);
                updateInterval = null;
            }
        });
    function getRandomColor() {
        return `#${Math.floor(Math.random() * 16777215).toString(16)}`;
    }

    consumptionGraph.render();
    productionGraph.render();

    xValue++;
}

export function startSimulation(scenario_name){
    return fetch("/api/v1/simulation/start", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
        },
    body: JSON.stringify({name: scenario_name})
    }).then((response) => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
        .then((data) => {
            generateConsumers(data);
            generateProducers(data);
            if (!updateInterval) {
                updateInterval = setInterval(updateDashboard, 1000);
            }

        })
        .catch((error) => {
            console.error("Error starting simulation:", error);
            alert("Failed to start simulation.");
        });
}

export function toggleStartStop(scenario_name) {

    if (startButton.textContent === "Start") {
        startButton.textContent = "Stop";
        startButton.classList.add("stop");

        showPopup();

    } else {
        startButton.textContent = "Start";
        startButton.classList.remove("stop");

        if (updateInterval) {
            clearInterval(updateInterval);
            updateInterval = null;
        }
    }
}

let total_satisfaction = 0;
let total_production = 0;

function getAssetsByRole(data, role){
    const assets = [];
    // Iterate over the key-value pairs of the JSON object
    Object.entries(data).forEach(([key, value]) => {
        if (value.role === role) {
            assets.push({ id: key, ...value }); // Add key as 'id' along with asset properties
            if(role === "Consumer") total_satisfaction += value.nominal_power
            else total_production += value.nominal_power
        }
    });

    return assets;
}

function generateConsumers(data){
    const role = "Consumer";

    const assets = getAssetsByRole(data, role);

    const consumersContainer = document.getElementById("consumers");
    consumersContainer.innerHTML = "";

    assets.forEach(asset => {
        let color = generateColorNearBase("#FF8A8A");

        // Save the color for reuse
        assetColors[asset.name] = color;

        const assetHtml = `
        <div class="consumption-unit">
            <div class="consumption-unit-info">
                <img src="/SimulationPage/img/washingMachine.svg" alt="Washing Machine">
                    <h3>${asset.name}</h3>
            </div>
            <div class="progress-bar-container">
                <div class="progress-bar" style="background-color: ${color}" id="${asset.name}-progress-bar">0 MW</div>
            </div>
            <div>
                <span id="${asset.name}-value-display" class="value-display">0 W / </span>
                <span id="${asset.name}-max-value">${asset.nominal_power}</span> W
            </div>
        </div>`
            consumersContainer.innerHTML += assetHtml;
        });

    let max_satisfaction_container = document.getElementById("max-satisfaction")
    max_satisfaction_container.innerText = total_satisfaction;
}

function generateProducers(data){
    const role = "Producer"
    const assets = getAssetsByRole(data, role);

    const producersContainer = document.getElementById("producers");
    producersContainer.innerHTML = "";

    assets.forEach(asset => {
        let color = generateColorNearBase("#FF8A8A");

        // Save the color for reuse
        assetColors[asset.name] = color;

        let imageSrc = "/SimulationPage/img/accumulator.svg"; // Default image
        if (asset.type === "solar_panel") {
            imageSrc = "/SimulationPage/img/solarPanel.svg";
        } else if (asset.type === "wind_turbine") {
            imageSrc = "/SimulationPage/img/windTurbine.svg";
        }

        const assetHtml = `
                <div class="production-unit">
                    <div class="production-unit-info">
                        <img src="${imageSrc}" alt="${asset.type}">
                        <h3>${asset.name}</h3>
                    </div>
                    <div class="progress-bar-container">
                        <div id="${asset.name}-progress-bar" class="progress-bar" style="background-color: ${color}">0 W</div>
                    </div>
                    <div>
                        <span id="${asset.name}-value-display" class="value-display">0 W / </span>
                        <span id="${asset.name}-max-value">${asset.nominal_power}</span> W
                    </div>
                </div>`
        producersContainer.innerHTML += assetHtml;

        assetsProducers.push(asset.name);
    });

    let max_production_container = document.getElementById("max-production")
    max_production_container.innerText = total_production;

}

startButton.addEventListener("click", toggleStartStop)