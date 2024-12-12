import React from 'react';

const TicketInfo = ({ stats }) => {
  return (
    <div className="ticket-info">
      <h3 className="ticket-info-title">System Statistics</h3>
      <div className="ticket-stats-grid">
        <div className="stat-box">
          <div className="stat-label">Total Tickets</div>
          <div className="stat-value">{stats.totalTickets}</div>
        </div>
        <div className="stat-box">
          <div className="stat-label">Max Capacity</div>
          <div className="stat-value">{stats.maxTicketCapacity}</div>
        </div>
        <div className="stat-box">
          <div className="stat-label">Vendors</div>
          <div className="stat-value">{stats.numOfVendors}</div>
        </div>
        <div className="stat-box">
          <div className="stat-label">Customers</div>
          <div className="stat-value">{stats.numOfCustomers}</div>
        </div>
      </div>
    </div>
  );
};

export default TicketInfo;