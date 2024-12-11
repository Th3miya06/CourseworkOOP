import React from 'react';

const LogDisplay = ({ logs }) => {
  
  return (
    <div className="log-display">
      <h3>System Logs</h3>
      <div className="log-container">
        {logs.map((log, index) => (
          <div key={index} className="log-entry">
            {log}
          </div>
        ))}
      </div>
    </div>
  );
};

export default LogDisplay;