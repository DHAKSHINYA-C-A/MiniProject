import React, { useState } from 'react';
import './lifeInsurance.css'
const InsuranceForm = () => {
  const [formData, setFormData] = useState({
    name: '',
    mobile: '',
    age: '',
    gender: '',
    policyProvider: '',
    annualIncome: '',
    occupation: '',
    education: '',
    propertyDocuments: null,
  });
  const [submittedData, setSubmittedData] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    setFormData((prevState) => ({
      ...prevState,
      propertyDocuments: file,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Perform form validation here
    
    // Set the submitted data
    setSubmittedData(formData);
  };

  return (
    <div className="form-container">
      <h1>Life Insurance Application</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="name">Name:</label>
          <input type="text" id="name" name="name" value={formData.name} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="mobile">Mobile:</label>
          <input type="text" id="mobile" name="mobile" value={formData.mobile} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="age">Age:</label>
          <input type="number" id="age" name="age" value={formData.age} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="gender">Gender:</label>
          <select id="gender" name="gender" value={formData.gender} onChange={handleChange} required>
            <option value="">Select</option>
            <option value="male">Male</option>
            <option value="female">Female</option>
            <option value="other">Other</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="policyProvider">Policy Provider:</label>
          <select id="policyProvider" name="policyProvider" value={formData.policyProvider} onChange={handleChange} required>
            <option value="">Select</option>
            <option value="provider1">Provider 1</option>
            <option value="provider2">Provider 2</option>
            <option value="provider3">Provider 3</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="annualIncome">Annual Income:</label>
          <input type="number" id="annualIncome" name="annualIncome" value={formData.annualIncome} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="occupation">Occupation:</label>
          <input type="text" id="occupation" name="occupation" value={formData.occupation} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="education">Education:</label>
          <input type="text" id="education" name="education" value={formData.education} onChange={handleChange} required />
        </div>
        <div className="form-group">
          <label htmlFor="propertyDocuments">Property Documents:</label>
          <input type="file" id="propertyDocuments" name="propertyDocuments" onChange={handleFileChange} required />
        </div>
        <div className="form-group">
          <button type="submit">Submit</button>
        </div>
      </form>
      {submittedData && (
        <div>
          <h2>Summary:</h2>
          <p>Name: {submittedData.name}</p>
          <p>Mobile: {submittedData.mobile}</p>
          <p>Age: {submittedData.age}</p>
          <p>Gender: {submittedData.gender}</p>
          <p>Policy Provider: {submittedData.policyProvider}</p>
          <p>Annual Income: {submittedData.annualIncome}</p>
          <p>Occupation: {submittedData.occupation}</p>
          <p>Education: {submittedData.education}</p>
          <p>Property Documents: {submittedData.propertyDocuments.name}</p>
        </div>
      )}
    </div>
  );
};

export default InsuranceForm;
