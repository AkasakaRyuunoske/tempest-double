let updateInterval = null;

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
    axisY: { title: "MW", includeZero: false, gridThickness: 1, gridDashType: "solid" },
    data: [
        {
            type: "line",
            color: washingMachineColor,
            name: "Washing Machine",
            showInLegend: true,
            dataPoints: washingMachineData,
        },
        {
            type: "line",
            color: refrigeratorColor,
            name: "Refrigerator",
            showInLegend: true,
            dataPoints: refrigeratorData,
        },
        {
            type: "line",
            color: airConditionerColor,
            name: "Air Conditioner",
            showInLegend: true,
            dataPoints: airConditionerData,
        }
    ]
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

function updateMWValues(currentId, maxId, currentValue, maxValue) {
    document.getElementById(currentId).textContent = currentValue.toFixed(2);
    document.getElementById(maxId).textContent = maxValue.toFixed(2);
}

function updateDashboard() {
    const washingMachineValue = Math.random() * maxCapacities.washingMachine;
    const refrigeratorValue = Math.random() * maxCapacities.refrigerator;
    const airConditionerValue = Math.random() * maxCapacities.airConditioner;

    const solarPanelValue = Math.random() * maxCapacities.solarPanel;
    const windTurbineValue = Math.random() * maxCapacities.windTurbine;
    const accumulatorValue = Math.random() * maxCapacities.accumulatorBar;

    const satisfactionValue = Math.random() * maxCapacities.satisfaction;
    const totalProduction = solarPanelValue + windTurbineValue;
    const totalAccumulatorCharge = Math.random() * maxCapacities.accumulator;

    updateProgressBar("washingMachineBar", washingMachineValue, maxCapacities.washingMachine, "washing-machine-value");
    updateProgressBar("refrigeratorBar", refrigeratorValue, maxCapacities.refrigerator, "refrigerator-value");
    updateProgressBar("airConditionerBar", airConditionerValue, maxCapacities.airConditioner, "air-conditioner-value");
    updateProgressBar("solarPanelBar", solarPanelValue, maxCapacities.solarPanel, "solar-panel-value");
    updateProgressBar("windTurbineBar", windTurbineValue, maxCapacities.windTurbine, "wind-turbine-value");
    updateProgressBar("accumulatorBar", accumulatorValue, maxCapacities.accumulatorBar, "accumulator-value");

    updateProgressBar("satisfactionBar", satisfactionValue, maxCapacities.satisfaction, "satisfaction-value");
    updateProgressBar("productionBar", totalProduction, maxCapacities.production, "production-value");
    updateProgressBar("accumulatorChargeBar", totalAccumulatorCharge, maxCapacities.accumulator, "accumulator-charge-value");

    updateMWValues("current-satisfaction", "max-satisfaction", satisfactionValue, maxCapacities.satisfaction);
    updateMWValues("current-production", "max-production", totalProduction, maxCapacities.production);
    updateMWValues("current-charge", "max-charge", totalAccumulatorCharge, maxCapacities.accumulator);

    washingMachineData.push({ x: xValue, y: washingMachineValue });
    refrigeratorData.push({ x: xValue, y: refrigeratorValue });
    airConditionerData.push({ x: xValue, y: airConditionerValue });

    productionData.push({ x: xValue, y: totalProduction });

    if (washingMachineData.length > 20) washingMachineData.shift();
    if (refrigeratorData.length > 20) refrigeratorData.shift();
    if (airConditionerData.length > 20) airConditionerData.shift();
    if (productionData.length > 20) productionData.shift();

    consumptionGraph.render();
    productionGraph.render();

    xValue++;
}

function startSimulation(){
    return fetch("/api/v1/simulation/start", {
    method: "POST",
    headers: {
        "Content-Type": "application/json"
        },
    body: JSON.stringify({name: "Flu"})
    })
}
function toggleStartStop() {
    const startButton = document.querySelector(".start-button button");

    if (startButton.textContent === "Start") {
        startSimulation().then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
            })
            .then((data) => {
                startButton.textContent = "Stop";
                startButton.classList.add("stop");
                console.log("Got some data too")
                console.log(data)
                // if (!updateInterval) {
                //     updateInterval = setInterval(updateDashboard, 1000);
                // }
            })
            .catch((error) => {
                console.error("Error starting simulation:", error);
                alert("Failed to start simulation.");
            });
    } else {
        startButton.textContent = "Start";
        startButton.classList.remove("stop");
        // if (updateInterval) {
        //     clearInterval(updateInterval);
        //     updateInterval = null;
        // }
    }
}

document.querySelector(".start-button button").addEventListener("click", toggleStartStop);

setProgressBarColors();

