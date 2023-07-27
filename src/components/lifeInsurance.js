import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import './lifeInsurance.css';

const InsuranceForm = () => {
  const navigate = useNavigate();

  const [fullName, setFullName] = useState('');
  const [age, setAge] = useState('');
  const [gender, setGender] = useState('');
  const [mobile, setPhone] = useState('');
  const [policyType, setPolicy] = useState('');

  let token = localStorage.getItem('token');

  const handleSubmit = async (e) => {
    e.preventDefault();

    console.log('Form submitted');
    console.log('Full Name:', fullName);
    console.log('age:', age);
    console.log('gender:', gender);
    console.log('policyType:', policyType);
    console.log('mobile:', mobile);

    try {
      const response = await axios.post(
        'http://localhost:8181/addroll',
        {
          fullName: fullName,
          age: age,
          gender: gender,
          policyType: policyType,
          mobile: mobile,
          annualIncome: e.target.annualIncome.value, // Include annualIncome from the form
          occupation: e.target.occupation.value, // Include occupation from the form
          education: e.target.education.value,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            'cache-control': 'no-control',
          },
        }
      );

      // Handle response as needed, e.g., show success message
      console.log(response.data);
      alert('Registration successful!');
      navigate('/policies');
    } catch (error) {
      console.log(error);
      window.alert('Invalid Credentials');
    }
  };

  return (
    <div className="form-container1">
      <div className="image-container1">
        <img
          src={
            'https://res.cloudinary.com/duj7wgdt8/image/upload/v1689222100/life-insurance-template-social-media-post_53876-119136_wslmce-removebg-preview_o3gdsq.png'
          }
          alt="Insurance"
        />
      </div>
      <div className="form-content1">
        <h1>
          Get Your Life <span className="blue">Insured</span> at Just ₹<span className="blue">99</span> per month
        </h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group1">
            <label htmlFor="name">Name:</label>
            <input type="text" id="name" name="name" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          </div>
          <div className="form-group1">
            <label htmlFor="mobile">Mobile:</label>
            <input type="text" id="mobile" name="mobile" value={mobile} onChange={(e) => setPhone(e.target.value)} required />
          </div>
          <div className="form-group1">
            <label htmlFor="age">Age:</label>
            <input type="number" id="age" name="age" value={age} onChange={(e) => setAge(e.target.value)} required />
          </div>
          <div className="form-group1">
            <label htmlFor="gender">Gender:</label>
            <select id="gender" name="gender" value={gender} onChange={(e) => setGender(e.target.value)} required>
              <option value="">Select</option>
              <option value="male">Male</option>
              <option value="female">Female</option>
              <option value="other">Other</option>
            </select>
          </div>
          <div className="form-group1">
            <label htmlFor="policyProvider">Policy Provider:</label>
            <select id="policyProvider" name="policyProvider" value={policyType} onChange={(e) => setPolicy(e.target.value)} required>
              <option value="">Select</option>
              <option value="provider1">Provider 1</option>
              <option value="provider2">Provider 2</option>
              <option value="provider3">Provider 3</option>
            </select>
          </div>
          <div className="form-group1">
            <label htmlFor="annualIncome">Annual Income:</label>
            <input type="number" id="annualIncome" name="annualIncome" required />
          </div>
          <div className="form-group1">
            <label htmlFor="occupation">Occupation:</label>
            <input type="text" id="occupation" name="occupation" required />
          </div>
          <div className="form-group1">
            <label htmlFor="education">Education:</label>
            <input type="text" id="education" name="education" required />
          </div>
          
          <div className="form-group1">
            <button type="submit">Submit</button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default InsuranceForm;
