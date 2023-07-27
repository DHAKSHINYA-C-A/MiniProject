import axios from "axios";
const URI='http://localhost:8080'
const getStudent = () => axios.get(`${URI}/student/getstu`)
const addStudent = (student) => axios.post(`${URI}/student/addstu`,student)
// const getProductbyId = (id)=> axios.get(`${URI}/product/find/${id}`)
const editStudent = (stu_id,student)=>axios.put(`${URI}/student/updatestu/${stu_id}`,student)
const deleteStudent=(stu_id)=>axios.delete(`${URI}/student/delete/${stu_id}`)
const getFeedback = () => axios.get(`${URI}/getfed`)
const addFeedback = (Feedback) => axios.post(`${URI}/addfed`,Feedback)
// const getProductbyId = (id)=> axios.get(`${URI}/product/find/${id}`)
const editFeedback = (feed_id,FeedBack)=>axios.put(`${URI}/updatefeed/${feed_id}`,FeedBack)
const deleteFeedback=(feed_id)=>axios.delete(`${URI}/delete/${feed_id}`)

export {getStudent,addStudent,deleteStudent,editStudent,getFeedback,addFeedback,deleteFeedback,editFeedback}