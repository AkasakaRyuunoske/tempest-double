const maxCapacities = {
    washingMachine: 5,
    refrigerator: 2,
    airConditioner: 3,
    solarPanel: 10,
    windTurbine: 15,
    accumulator: 20
};

const consumptionGraph = new CanvasJS.Chart("consumptionGraph", {
    title: { text: "Consumption Graph" },
    axisY: { title: "MW", includeZero: false },
    data: [{ type: "line", dataPoints: [] }],
});

const productionGraph = new CanvasJS.Chart("productionGraph", {
    title: { text: "Production Graph" },
    axisY: { title: "MW", includeZero: false },
    data: [{ type: "line", dataPoints: [] }],
});

let consumptionData = [];
let productionData = [];
let xValue = 0;

function updateDashboard() {
    // Update graph data
    const consumptionValue = Math.random() * 100;
    const productionValue = Math.random() * 100;

    consumptionData.push({ x: xValue, y: consumptionValue });
    productionData.push({ x: xValue, y: productionValue });

    if (consumptionData.length > 20) consumptionData.shift();
    if (productionData.length > 20) productionData.shift();

    consumptionGraph.options.data[0].dataPoints = consumptionData;
    productionGraph.options.data[0].dataPoints = productionData;

    consumptionGraph.render();
    productionGraph.render();

    updateProgressBar("washingMachineBar", Math.random() * maxCapacities.washingMachine, maxCapacities.washingMachine);
    updateProgressBar("refrigeratorBar", Math.random() * maxCapacities.refrigerator, maxCapacities.refrigerator);
    updateProgressBar("airConditionerBar", Math.random() * maxCapacities.airConditioner, maxCapacities.airConditioner);
    updateProgressBar("solarPanelBar", Math.random() * maxCapacities.solarPanel, maxCapacities.solarPanel);
    updateProgressBar("windTurbineBar", Math.random() * maxCapacities.windTurbine, maxCapacities.windTurbine);
    updateProgressBar("accumulatorBar", Math.random() * maxCapacities.accumulator, maxCapacities.accumulator);

    xValue++;
}

function updateProgressBar(id, value, maxCapacity) {
    const bar = document.getElementById(id);
    const percentage = (value / maxCapacity) * 100;
    bar.style.width = percentage + "%";
    bar.textContent = value.toFixed(2) + " MW";
}

setInterval(updateDashboard, 1000);