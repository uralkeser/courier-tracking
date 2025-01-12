const WebSocket = require('ws');
const moment = require('moment');

const PORT = 9000; // Sender application runs on port 9000
const wss = new WebSocket.Server({ port: PORT });

console.log(`WebSocket server is running on ws://localhost:${PORT}`);

// Generate random location data
function generateRandomLocation() {
  const lat = 40.9 + Math.random() * 0.2; // Latitude around Istanbul
  const lng = 29.0 + Math.random() * 0.2; // Longitude around Istanbul
  return { lat, lng };
}

// Generate courier data
function generateCourierData() {
  return {
    time: moment(new Date()).utc().format('YYYY-MM-DDTHH:mm:ss'),
    courierId: Math.floor(Math.random() * 5) + 1, // Random courier ID (1-5)
    ...generateRandomLocation(),
  };
}

// Broadcast courier data every 30 seconds
setInterval(() => {
  const data = generateCourierData();
  console.log('Sending data:', data);
  wss.clients.forEach((client) => {
    if (client.readyState === WebSocket.OPEN) {
      client.send(JSON.stringify(data));
    }
  });
}, 1000);
