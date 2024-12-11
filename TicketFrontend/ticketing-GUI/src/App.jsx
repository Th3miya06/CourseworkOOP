// App.jsx
import { useState ,useEffect } from 'react';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import LogDisplay from './components/LogDisplay';
import { websocketService } from './services/websocket';

const App = () => {
  const [isRunning, setIsRunning] = useState(false);
  const [logs, setLogs] = useState([]);
  const [error, setError] = useState(null);
  const [isConfigured, setIsConfigured] = useState(false);

  useEffect(() => {
    console.log('Setting up WebSocket connection...');
    
    try {
        // Initialize WebSocket connection
        websocketService.connect();
        
        // Create message handler
        const handleMessage = (message) => {
            console.log('Received message:', message);
            addLog(message);
        };
        
        // Add message handler
        websocketService.addMessageHandler(handleMessage);
        
        // Return cleanup function
        return () => {
            console.log('Cleaning up WebSocket connection...');
            websocketService.removeMessageHandler(handleMessage);
            websocketService.disconnect();
        };
    } catch (err) {
        console.error('Error in WebSocket setup:', err);
        setError('Failed to establish WebSocket connection');
    }
}, []);






  const handleConfigSubmit = async (config) => {
    try {
      const response = await fetch('http://localhost:8080/config', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(config)
      });
      
      if (!response.ok) {
        throw new Error('Failed to save configuration');
      }
      
      setIsConfigured(true);
      addLog('Configuration saved successfully');
      setError(null);
    } catch (err) {
      setError('Failed to save configuration: ' + err.message);
      addLog('Error: Failed to save configuration');
      setIsConfigured(false);
    }
  };

  const handleStart = async () => {

    if (!isConfigured) {
      setError('System is not configured');
      addLog('Error: Cannot start - System is not configured');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/start', {
        method: 'POST'
      });
      
      if (!response.ok) {
        throw new Error('Failed to start system');
      }
      
      setIsRunning(true);
      addLog('System started');
    } catch (err) {
      setError('Failed to start system: ' + err.message);
      addLog('Error: Failed to start system');
    }
  };

  const handleStop = async () => {
    try {
      const response = await fetch('http://localhost:8080/stop', {
        method: 'POST'
      });
      
      if (!response.ok) {
        throw new Error('Failed to stop system');
      }
      
      setIsRunning(false);
      addLog('System stopped');
    } catch (err) {
      setError('Failed to stop system: ' + err.message);
      addLog('Error: Failed to stop system');
    }
  };

  const addLog = (message) => {
    const timestamp = new Date().toLocaleTimeString();
    setLogs(prev => [...prev, `${timestamp} - ${message}`]);
  };

  console.log('Rendering App component');

  return (
    <div className="app-container">
      <h1>Ticket Management System</h1>
      
      {error && (
        <div className="error-message">
          {error}
        </div>
      )}

      <ConfigurationForm onSubmit={handleConfigSubmit} />
      
      <ControlPanel 
        onStart={handleStart}
        onStop={handleStop}
        isRunning={isRunning}
      />
      
      <LogDisplay logs={logs} />
    </div>
  );
};

export default App;