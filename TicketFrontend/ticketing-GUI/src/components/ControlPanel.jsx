import React from 'react';

const ControlPanel = ({ onStart, onStop, isRunning }) => {
  return (
    <div className="control-panel">
      <button 
        onClick={onStart} 
        disabled={isRunning} 
        className="control-button start-button"
      >
        Start System
      </button>
      <button 
        onClick={onStop} 
        disabled={!isRunning} 
        className="control-button stop-button"
      >
        Stop System
      </button>
    </div>
  );
};

export default ControlPanel;