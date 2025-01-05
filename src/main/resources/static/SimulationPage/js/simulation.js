import {showPopup} from "./pop_up.js";

let updateInterval = null;
let scenario_name = null;
let assetsProducers = [];
const startButton = document.querySelector(".start-button button");

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

let washingMachineData = [];
let refrigeratorData = [];
let airConditionerData = [];
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
            console.log("Got some data from simulate")
            console.log(data)

            Object.entries(data).forEach(([name, value]) => {
                console.log("Values we are dealing with: ")
                console.log(`${name}-progress-bar`)
                console.log(`${name}-value-display`)
                console.log(`${value} --> value`)

                // Handle specific cases for total energy consumed and produced
                if (name === "total_energy_consumed") {
                    document.getElementById("current-satisfaction").innerText = value.toFixed(2);

                    return;
                }

                if (name === "total_energy_produced") {
                    document.getElementById("current-production").innerText = value.toFixed(2);

                    return;
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
                            color: getRandomColor(),
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
                            color: getRandomColor(),
                            name: name,
                            showInLegend: true,
                            dataPoints: [{ x: xValue, y: value }],
                        });
                    }
                }

                updateProgressBar(`${name}-progress-bar`, value, 3000, `${name}-value-display`);
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
    // updateProgressBar("refrigeratorBar", refrigeratorValue, maxCapacities.refrigerator, "refrigerator-value");
    // updateProgressBar("airConditionerBar", airConditionerValue, maxCapacities.airConditioner, "air-conditioner-value");
    // updateProgressBar("solarPanelBar", solarPanelValue, maxCapacities.solarPanel, "solar-panel-value");
    // updateProgressBar("windTurbineBar", windTurbineValue, maxCapacities.windTurbine, "wind-turbine-value");
    // updateProgressBar("accumulatorBar", accumulatorValue, maxCapacities.accumulatorBar, "accumulator-value");
    //
    // updateProgressBar("satisfactionBar", satisfactionValue, maxCapacities.satisfaction, "satisfaction-value");
    // updateProgressBar("productionBar", totalProduction, maxCapacities.production, "production-value");
    // updateProgressBar("accumulatorChargeBar", totalAccumulatorCharge, maxCapacities.accumulator, "accumulator-charge-value");

    // updateWValues("current-satisfaction", "max-satisfaction", satisfactionValue, maxCapacities.satisfaction);
    // updateWValues("current-production", "max-production", totalProduction, maxCapacities.production);
    // updateWValues("current-charge", "max-charge", totalAccumulatorCharge, maxCapacities.accumulator);

    // washingMachineData.push({ x: xValue, y: washingMachineValue });
    // refrigeratorData.push({ x: xValue, y: refrigeratorValue });
    // airConditionerData.push({ x: xValue, y: airConditionerValue });
    //
    // productionData.push({ x: xValue, y: totalProduction });
    //
    // if (washingMachineData.length > 20) washingMachineData.shift();
    // if (refrigeratorData.length > 20) refrigeratorData.shift();
    // if (airConditionerData.length > 20) airConditionerData.shift();
    // if (productionData.length > 20) productionData.shift();

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
            console.log("Got some data too")
            console.log(data)
            generateConsumers(data);
            generateProducers(data);
            if (!updateInterval) {
                updateInterval = setInterval(updateDashboard, 1000);
            }

            // let start_button = document.getElementById('start-button');
            // // Clone the element
            // const newElement = start_button.cloneNode(true);
            //
            // // Replace the old element with the new one
            // start_button.parentNode.replaceChild(newElement, start_button);
            //
            // start_button.addEventListener('click', toggleStartStop);

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

    assets.forEach(asset => {
        let color = generateColorNearBase("#FF8A8A");
        const assetHtml = `
        <div class="consumption-unit">
            <div class="consumption-unit-info">
                <img src="/SimulationPage/img/washingMachine.svg" alt="Washing Machine">
                    <h3>${asset.name}</h3>
            </div>
            <div class="progress-bar-container">
                <div class="progress-bar" style="background-color: ${color}" id="${asset.name}-progress-bar">0 MW</div>
            </div>
            <div class="value-display" id="${asset.name}-value-display">0 MW / ${asset.nominal_power} W</div>
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

    assets.forEach(asset => {
        let color = generateColorNearBase("#FF8A8A");
        const assetHtml = `
                <div class="production-unit">
                    <div class="production-unit-info">
                        <img src="/SimulationPage/img/accumulator.svg" alt="Accumulator">
                        <h3>${asset.name}</h3>
                    </div>
                    <div class="progress-bar-container">
                        <div id="${asset.name}-progress-bar" class="progress-bar" style="background-color: ${color}">0 W</div>
                    </div>
                    <div id="${asset.name}-value-display" class="value-display">0 W / ${asset.nominal_power} W</div>
                </div>`
        producersContainer.innerHTML += assetHtml;

        assetsProducers.push(asset.name);
    });

    let max_production_container = document.getElementById("max-production")
    max_production_container.innerText = total_production;

}

// setProgressBarColors();

startButton.addEventListener("click", toggleStartStop)