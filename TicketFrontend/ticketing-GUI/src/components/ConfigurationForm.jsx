// ConfigurationForm.jsx
import { useState } from 'react';

const ConfigurationForm = ({ onSubmit }) => {
    // State for form fields
    const [totalTickets, setTotalTickets] = useState('');
    const [ticketReleaseRate, setTicketReleaseRate] = useState('');
    const [customerRetrievalRate, setCustomerRetrievalRate] = useState('');
    const [maxTicketCapacity, setMaxTicketCapacity] = useState('');
    const [numOfVendors, setNumOfVendors] = useState('');
    const [numOfCustomers, setNumOfCustomers] = useState('');
    const [quantity, setQuantity] = useState('');
    const [validationErrors, setValidationErrors] = useState({});

    // Validation function
    const validateForm = () => {
        const errors = {};
        
        // Convert string values to numbers for comparison
        const totalTicketsNum = parseInt(totalTickets);
        const maxTicketCapacityNum = parseInt(maxTicketCapacity);
        const quantityNum = parseInt(quantity);

        // Basic validation for positive numbers
        if (totalTicketsNum <= 0) {
            errors.totalTickets = 'Total Tickets must be greater than 0';
        }

        if (maxTicketCapacityNum <= 0) {
            errors.maxTicketCapacity = 'Max Ticket Capacity must be greater than 0';
        }

        if (quantityNum <= 0) {
            errors.quantity = 'Quantity must be greater than 0';
        }

        // Business logic validations
        if (maxTicketCapacityNum > totalTicketsNum) {
            errors.maxTicketCapacity = 'Max Ticket Capacity cannot be greater than Total Tickets';
        }

        if (quantityNum > totalTicketsNum) {
            errors.quantity = 'Quantity cannot be greater than Total Tickets';
        }

        return errors;
    };

    // Form submission handler
    const handleSubmit = (e) => {
        e.preventDefault();
        
        // Perform validation
        const errors = validateForm();
        
        if (Object.keys(errors).length > 0) {
            // If there are errors, set them and prevent submission
            setValidationErrors(errors);
            return;
        }

        // Clear any existing validation errors
        setValidationErrors({});

        // If validation passes, submit the form with parsed integer values
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
                <input 
                    type="number" 
                    min="1" 
                    value={totalTickets} 
                    onChange={(e) => setTotalTickets(e.target.value)} 
                    className={`configuration-input ${validationErrors.totalTickets ? 'input-error' : ''}`}
                    required
                />
                {validationErrors.totalTickets && (
                    <span className="error-text">{validationErrors.totalTickets}</span>
                )}
            </label>

            <label>
                Ticket Release Rate:
                <input 
                    type="number" 
                    min="1" 
                    value={ticketReleaseRate} 
                    onChange={(e) => setTicketReleaseRate(e.target.value)} 
                    className="configuration-input"
                    required
                />
            </label>

            <label>
                Customer Retrieval Rate:
                <input 
                    type="number" 
                    min="1" 
                    value={customerRetrievalRate} 
                    onChange={(e) => setCustomerRetrievalRate(e.target.value)} 
                    className="configuration-input"
                    required
                />
            </label>

            <label>
                Max Ticket Capacity:
                <input 
                    type="number" 
                    min="1" 
                    value={maxTicketCapacity} 
                    onChange={(e) => setMaxTicketCapacity(e.target.value)} 
                    className={`configuration-input ${validationErrors.maxTicketCapacity ? 'input-error' : ''}`}
                    required
                />
                {validationErrors.maxTicketCapacity && (
                    <span className="error-text">{validationErrors.maxTicketCapacity}</span>
                )}
            </label>

            <label>
                Number of Vendors:
                <input 
                    type="number" 
                    min="1" 
                    value={numOfVendors} 
                    onChange={(e) => setNumOfVendors(e.target.value)} 
                    className="configuration-input"
                    required
                />
            </label>

            <label>
                Number of Customers:
                <input 
                    type="number" 
                    min="1" 
                    value={numOfCustomers} 
                    onChange={(e) => setNumOfCustomers(e.target.value)} 
                    className="configuration-input"
                    required
                />
            </label>

            <label>
                Quantity:
                <input 
                    type="number" 
                    min="1" 
                    value={quantity} 
                    onChange={(e) => setQuantity(e.target.value)} 
                    className={`configuration-input ${validationErrors.quantity ? 'input-error' : ''}`}
                    required
                />
                {validationErrors.quantity && (
                    <span className="error-text">{validationErrors.quantity}</span>
                )}
            </label>

            <button type="submit" className="configuration-submit-button">
                Submit Configuration
            </button>
        </form>
    );
};

export default ConfigurationForm;