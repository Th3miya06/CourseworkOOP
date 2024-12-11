import { useState } from 'react';

const ConfigurationForm = ({ onSubmit }) => {
  const [totalTickets, setTotalTickets] = useState('');
  const [ticketReleaseRate, setTicketReleaseRate] = useState('');
  const [customerRetrievalRate, setCustomerRetrievalRate] = useState('');
  const [maxTicketCapacity, setMaxTicketCapacity] = useState('');
  const [numOfVendors, setNumOfVendors] = useState('');
  const [numOfCustomers, setNumOfCustomers] = useState('');
  const [quantity, setQuantity] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({
      totalTickets: parseInt(totalTickets),
      ticketReleaseRate: parseInt(ticketReleaseRate),
      customerRetrievalRate: parseInt(customerRetrievalRate),
      maxTicketCapacity: parseInt(maxTicketCapacity),
      numOfVendors: parseInt(numOfVendors),
      numOfCustomers: parseInt(numOfCustomers),
      quantity: parseInt(quantity),
    });
  };

  return (
    <form className="configuration-form" onSubmit={handleSubmit}>
      <label>
        Total Tickets:
        <input type="number" min="1" value={totalTickets} onChange={(e) => setTotalTickets(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Ticket Release Rate:
        <input type="number" min="1" value={ticketReleaseRate} onChange={(e) => setTicketReleaseRate(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Customer Retrieval Rate:
        <input type="number" min="1" value={customerRetrievalRate} onChange={(e) => setCustomerRetrievalRate(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Max Ticket Capacity:
        <input type="number" min="1" value={maxTicketCapacity} onChange={(e) => setMaxTicketCapacity(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Number of Vendors:
        <input type="number" min="1" value={numOfVendors} onChange={(e) => setNumOfVendors(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Number of Customers:
        <input type="number" min="1" value={numOfCustomers} onChange={(e) => setNumOfCustomers(e.target.value)} className="configuration-input" required/>
      </label>
      <label>
        Quantity:
        <input type="number" min="1" value={quantity} onChange={(e) => setQuantity(e.target.value)} className="configuration-input" required/>
      </label>
      <button type="submit" className="configuration-submit-button">
        Submit Configuration
      </button>
    </form>
  );
};

export default ConfigurationForm;