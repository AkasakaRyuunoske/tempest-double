export function createNode(id, label, x, y, node_info) {
    const node = document.createElement("div");
    node.id = id;
    node.className = "node";
    node.innerHTML = `
                <span>${label}</span>
                <div class="connection-handle"></div>
                <br>
                <button class="delete-btn" data-id="${id}">✖</button>
            `;
    node.style.left = `${x}px`;
    node.style.top = `${y}px`;
    canvas.appendChild(node);

    node.info = node_info

    // Make the node draggable
    jsPlumb.draggable(node, {containment: "parent"});

    // Enable text editing on double-click
    enableTextEditing(node);

    // Add connection points
    enableConnectionPoints(node);

    // allow a node to be deleted
    const deleteBtn = node.querySelector(".delete-btn");
    deleteBtn.addEventListener("click", (event) => {
        console.log(node.info)
        console.log("node.info")
        const nodeId = event.target.dataset.id;
        deleteNode(nodeId);
    });

    return node;
}

function deleteNode(nodeId) {
    // Remove all connections related to the node
    jsPlumb.deleteConnectionsForElement(nodeId);

    // Remove the node from the DOM
    const nodeElement = document.getElementById(nodeId);
    if (nodeElement) {
        nodeElement.remove();
    }

    // Optionally clean up endpoints
    jsPlumb.remove(nodeId);
}

export function enableTextEditing(node) {
    node.addEventListener("dblclick", function () {
        const currentText = node.querySelector("span").innerText;
        const input = document.createElement("input");
        input.type = "text";
        input.value = currentText;
        input.style.width = "100%";
        input.style.height = "100%";
        input.style.border = "none";
        input.style.textAlign = "center";
        input.style.fontSize = "14px";

        let id = node.id
        // Replace the text with an input field
        node.innerHTML = `<div class="connection-handle"></div>`;
        node.querySelector(".connection-handle").after(input);
        input.focus();

        // Restore the text when editing is done
        input.addEventListener("blur", function () {
            const updatedText = input.value.trim();
            node.innerHTML = `<span>${updatedText || currentText}</span>
                              <div class="connection-handle"></div>
                              <button class="delete-btn" data-id="${id}">✖</button>`;
            // Add connection points
            enableConnectionPoints(node);
            const deleteBtn = node.querySelector(".delete-btn");
            deleteBtn.addEventListener("click", (event) => {
                const nodeId = event.target.dataset.id;
                deleteNode(nodeId);
            });
        });

        // Handle Enter key
        input.addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                input.blur();
            }
        });
    });
}

function enableConnectionPoints(node) {
    const positions = ["Top", "Bottom", "Left", "Right"];

    positions.forEach((position) => {
        const connectionHandle = document.createElement("div");
        connectionHandle.className = "connection-handle";
        connectionHandle.dataset.position = position.toLowerCase();
        node.appendChild(connectionHandle);

        // Style each connection handle dynamically
        connectionHandle.style.position = "absolute";
        connectionHandle.style.width = "10px";
        connectionHandle.style.height = "10px";
        connectionHandle.style.backgroundColor = "blue";
        connectionHandle.style.borderRadius = "50%";
        connectionHandle.style.cursor = "pointer";

        switch (position) {
            // Uncomment to allow connections points in top/bottom positions.
//                    case "Top":
//                        connectionHandle.style.top = "-5px";
//                        connectionHandle.style.left = "50%";
//                        connectionHandle.style.transform = "translateX(-50%)";
//                        break;
//                    case "Bottom":
//                        connectionHandle.style.top = "35px";
//                        connectionHandle.style.left = "50%";
//                        connectionHandle.style.transform = "translateX(-50%)";
//                        break;
            case "Left":
                connectionHandle.style.left = "-5px";
                connectionHandle.style.top = "50%";
                connectionHandle.style.transform = "translateY(-50%)";
                break;
            case "Right":
                connectionHandle.style.right = "-5px";
                connectionHandle.style.top = "50%";
                connectionHandle.style.transform = "translateY(-50%)";
                break;
        }

        jsPlumb.makeSource(connectionHandle, {
            parent: node,
            anchor: position,
            connector: ["Straight"],
            connectorStyle: {stroke: "blue", strokeWidth: 2},
            endpoint: ["Dot", {radius: 5}],
            endpointStyle: {fill: "blue"},
        });
    });

    jsPlumb.makeTarget(node, {
        anchor: "Continuous",
        endpoint: ["Dot", {radius: 5}],
        endpointStyle: {fill: "blue"},
    });
}


// Prevent duplicate connections
jsPlumb.bind("beforeDrop", function (info) {
    const existingConnections = jsPlumb.getConnections({
        source: info.sourceId,
        target: info.targetId,
    });

    if (existingConnections.length > 0) {
        console.warn("Duplicate connection detected!");
        return false;
    }
    return true;
});

// Apply styles for connection handles
const style = document.createElement("style");
style.textContent = `
            .connection-handle {
                width: 10px;
                height: 10px;
                background-color: blue;
                border-radius: 50%;
                position: absolute;
                right: -5px;
                top: 50%;
                transform: translateY(-50%);
                cursor: pointer;
            }
        `;
document.head.appendChild(style);

document.addEventListener("DOMContentLoaded", function () {
    jsPlumb.ready(function () {
        jsPlumb.setContainer("canvas");

        // Get canvas and add initial nodes
        const canvas = document.getElementById("canvas");

        const node1 = createNode("node1", "Wind Turbine", 50, 50, "example node");
        const node2 = createNode("node2", "Air Condition", 200, 50, "example node");
        const node3 = createNode("node3", "Generic Consumer", 350, 50, "example node");

        // Connect initial nodes
        jsPlumb.connect({
            source: "node1",
            target: "node2",
            anchors: ["Right", "Left"],
            connector: "Straight",
            paintStyle: {stroke: "blue", strokeWidth: 2},
            endpoint: ["Dot", {radius: 5}],
            endpointStyle: {fill: "blue"},
        });

        jsPlumb.connect({
            source: "node2",
            target: "node3",
            anchors: ["Right", "Left"],
            connector: "Straight",
            paintStyle: {stroke: "blue", strokeWidth: 2},
            endpoint: ["Dot", {radius: 5}],
            endpointStyle: {fill: "red"},
        });

        function loadCanvasState(canvasState) {
            // Clear the canvas
            const canvas = document.getElementById("canvas");
            canvas.innerHTML = ""; // Remove all nodes from the DOM
            jsPlumb.reset(); // Reset jsPlumb (removes all connections)

            // Create nodes
            const nodes = canvasState.topology.nodes;
            nodes.forEach((node) => {
                const newNode = createNode(node.id, node.name, node.position.x, node.position.y);
                enableConnectionPoints(newNode); // Add connection points to the node
            });

            // Create connections
            const connections = canvasState.topology.connections;
            connections.forEach((connection) => {
                jsPlumb.connect({
                    source: connection.source,
                    target: connection.target,
                    anchors: ["Continuous", "Continuous"], // Ensure connections work dynamically
                    connector: "Straight",
                    paintStyle: {stroke: "blue", strokeWidth: 2},
                    endpoint: ["Dot", {radius: 5}],
                    endpointStyle: {fill: "blue"},
                });
            });

            console.log("Canvas state loaded successfully!");
        }

        document.getElementById("load").addEventListener("click", function () {
            let name = document.getElementById("scenario_name")

            fetch("/api/v1/scenario/" + name.value, {
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
                    console.log("Successfully saved canvas state:", data);
                    alert("Canvas state saved successfully!");
                })
                .catch((error) => {
                    console.error("Error saving canvas state:", error);
                    alert("Failed to save canvas state.");
                });

        });
    });
});

function saveCanvasState() {
    const nodes = document.querySelectorAll(".node");
    const nodeData = [];
    const connectionData = [];

    // Gather node information
    nodes.forEach((node) => {
        const id = node.id;
        const name = node.querySelector("span").innerText;
        const {left, top} = node.style;
        nodeData.push({
            id,
            name,
            position: {
                x: parseInt(left, 10),
                y: parseInt(top, 10),
            },
        });
    });

    // Gather connection information
    const connections = jsPlumb.getAllConnections();
    connections.forEach((connection) => {
        connectionData.push({
            source: connection.source.id,
            target: connection.target.id,
        });
    });

    // Combine into a single JSON object
    const canvasState = {
        nodes: nodeData,
        connections: connectionData,
    };

    return canvasState;
}

export function saveTopology(){
    const name = document.getElementById("scenario-name").value;
    console.log("Saving topology... " + name)

    const canvasState = saveCanvasState();

    let scenario = {
        "name": name,
        "environmentConfiguration":
            {"field": "value"},
        "assets":
            {"asset1": 1, "asset2": 3},
        topology: canvasState,
        "description": "None"
    }

    fetch("/api/v1/scenario", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(scenario),
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log("Successfully saved canvas state:", data);
            alert("Canvas state saved successfully!");
        })
        .catch((error) => {
            console.error("Error saving canvas state:", error);
            alert("Failed to save canvas state.");
        });
}

function centerNodesOnResize() {
    // Get the canvas and its bounding rectangle for position calculations
    const canvas = document.getElementById("canvas");
    const canvasRect = canvas.getBoundingClientRect();

    // Select all nodes on the canvas
    const nodes = document.querySelectorAll(".node");

    // Define the approximate width and height of a node
    const nodeWidth = 120;
    const nodeHeight = 70;

    // Calculate the center of the canvas
    const canvasCenterX = canvasRect.width / 2;
    const canvasCenterY = canvasRect.height / 2;

    // Calculate grid size (based on number of nodes) for arranging them
    const gridSize = Math.ceil(Math.sqrt(nodes.length));
    const nodeSpacing = 150;  // Space between each node

    // Calculate the starting position for the first node to keep it centered
    const startX = Math.max(0, canvasCenterX - (gridSize * nodeSpacing) / 2);
    const startY = Math.max(0, canvasCenterY - (gridSize * nodeSpacing) / 2);

    let row = 0, col = 0;  // Row and column counters for grid layout

    // Loop through each node to position them on the canvas
    nodes.forEach((node) => {
        // Calculate X and Y position for each node based on grid layout
        let x = startX + col * nodeSpacing;
        let y = startY + row * nodeSpacing;

        // Ensure node stays within the canvas bounds
        x = Math.min(Math.max(x, 0), canvasRect.width - nodeWidth);
        y = Math.min(Math.max(y, 0), canvasRect.height - nodeHeight);

        // Set the position of the node on the canvas
        node.style.left = `${x}px`;
        node.style.top = `${y}px`;

        // Move to the next column, or reset to the next row when reaching the grid size
        col++;
        if (col >= gridSize) {
            col = 0;
            row++;
        }

        // Reinitialize draggable behavior for each node after repositioning
        jsPlumb.draggable(node, {containment: "parent"});
    });

    // Repaint all jsPlumb connections to match the new node positions
    jsPlumb.repaintEverything();
}

// Trigger the centering function on window resize and initial load
window.addEventListener('load', centerNodesOnResize);
window.addEventListener("resize", centerNodesOnResize);
