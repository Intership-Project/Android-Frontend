import React, { useState, useEffect } from "react";
import FacultySidebar from "../components/FacultySidebar";
import "../components/FacultySidebar.css"; 
import "./AddFacultyFeedback.css";
import { toast } from "react-toastify";
import {
  addFacultyFeedback,
  fetchSubjects,
  fetchFaculties,
  fetchFeedbackTypes,
  fetchModuleTypes,
  fetchFacultyBatches,
  fetchMyCourse,
} from "../services/addfacultyfeedback";

function AddFacultyFeedback() {
  const [courseId, setCourseId] = useState("");
  const [batchId, setBatchId] = useState("");
  const [subjectId, setSubjectId] = useState("");
  const [facultyId, setFacultyId] = useState("");
  const [feedbackTypeId, setFeedbackTypeId] = useState("");
  const [feedbackModuleTypeId, setFeedbackModuleTypeId] = useState("");
  const [date, setDate] = useState("");
  const [pdfFile, setPdfFile] = useState(null);

  const [batches, setBatches] = useState([]);
  const [facultyRoleId, setFacultyRoleId] = useState(null);
  const [faculties, setFaculties] = useState([]);
  const [subjects, setSubjects] = useState([]);
  const [feedbackTypes, setFeedbackTypes] = useState([]);
  const [feedbackModuleTypes, setFeedbackModuleTypes] = useState([]);

  // Load initial data
  useEffect(() => {
    async function loadData() {
      const courseRes = await fetchMyCourse();
      if (courseRes.status === "success" && courseRes.data) {
        setCourseId(courseRes.data.course_id);
        const subjRes = await fetchSubjects(courseRes.data.course_id);
        if (subjRes.status === "success") {
          const data = subjRes.data;
          setSubjects(Array.isArray(data) ? data : [data]);
        }
      }
      const facRes = await fetchFaculties();
      if (facRes.status === "success") setFaculties(facRes.data || []);
      const fbTypeRes = await fetchFeedbackTypes();
      if (fbTypeRes.status === "success") setFeedbackTypes(fbTypeRes.data || []);
    }
    loadData();
  }, []);

  useEffect(() => {
    async function loadModuleTypes() {
      if (!feedbackTypeId) return;
      const res = await fetchModuleTypes(feedbackTypeId);
      if (res.status === "success") setFeedbackModuleTypes(res.data || []);
    }
    loadModuleTypes();
  }, [feedbackTypeId]);

  useEffect(() => {
    async function loadBatches() {
      if (!facultyId) {
        setBatches([]);
        setFacultyRoleId(null);
        setBatchId("");
        return;
      }
      const res = await fetchFacultyBatches(facultyId);
      if (res.status === "success") {
        setBatches(res.batches || []);
        setFacultyRoleId(res.role_id);
        if (res.role_id !== 1) setBatchId("");
      }
    }
    loadBatches();
  }, [facultyId]);

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!subjectId || !facultyId || !feedbackTypeId || !feedbackModuleTypeId || !date) {
      toast.warn("Please fill all required fields");
      return;
    }

    const result = await addFacultyFeedback({
      courseId,
      batchId: facultyRoleId === 1 ? batchId : null,
      subjectId,
      facultyId,
      moduleTypeId: feedbackModuleTypeId,
      feedbackTypeId,
      date,
      pdfFile,
    });

    if (result.status === "success") {
      toast.success("Feedback added successfully");
      setBatchId("");
      setSubjectId("");
      setFacultyId("");
      setFeedbackTypeId("");
      setFeedbackModuleTypeId("");
      setDate("");
      setPdfFile(null);
      setBatches([]);
      setFeedbackModuleTypes([]);
    } else {
      toast.error(result.error || "Something went wrong");
    }
  };

  return (
    <div className="app-wrapper">
      <FacultySidebar />
      <div className="main-content">
        <div className="container">
          <h2>Add Faculty Feedback</h2>
          <form onSubmit={onSubmit}>
            {/* Faculty */}
            <div className="form-group">
              <label>Faculty</label>
              <select value={facultyId} onChange={(e) => setFacultyId(e.target.value)}>
                <option value="">-- Select Faculty --</option>
                {faculties.map((f) => (
                  <option key={f.faculty_id} value={f.faculty_id}>{f.facultyname}</option>
                ))}
              </select>
            </div>

            {/* Batch */}
            {facultyRoleId === 1 && (
              <div className="form-group">
                <label>Batch</label>
                <select value={batchId} onChange={(e) => setBatchId(e.target.value)}>
                  <option value="">-- Select Batch --</option>
                  {batches.map((b) => (
                    <option key={b.batch_id} value={b.batch_id}>{b.batchname}</option>
                  ))}
                </select>
              </div>
            )}

            {/* Subject */}
            <div className="form-group">
              <label>Subject</label>
              <select value={subjectId} onChange={(e) => setSubjectId(e.target.value)}>
                <option value="">-- Select Subject --</option>
                {subjects.map((s) => (
                  <option key={s.subject_id} value={s.subject_id}>{s.subjectname}</option>
                ))}
              </select>
            </div>

            {/* Feedback Type */}
            <div className="form-group">
              <label>Feedback Type</label>
              <select value={feedbackTypeId} onChange={(e) => setFeedbackTypeId(e.target.value)}>
                <option value="">-- Select Feedback Type --</option>
                {feedbackTypes.map((t) => (
                  <option key={t.feedbacktype_id} value={t.feedbacktype_id}>{t.fbtypename}</option>
                ))}
              </select>
            </div>

            {/* Module Type */}
            <div className="form-group">
              <label>Feedback Module Type</label>
              <select value={feedbackModuleTypeId} onChange={(e) => setFeedbackModuleTypeId(e.target.value)}>
                <option value="">-- Select Module Type --</option>
                {feedbackModuleTypes.map((m) => (
                  <option key={m.feedbackmoduletype_id} value={m.feedbackmoduletype_id}>{m.fbmoduletypename}</option>
                ))}
              </select>
            </div>

            {/* Date */}
            <div className="form-group">
              <label>Date</label>
              <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
            </div>

            {/* PDF */}
            <div className="form-group">
              <label>PDF File</label>
              <input type="file" onChange={(e) => setPdfFile(e.target.files[0])} />
            </div>

            <button type="submit" className="btn btn-success">Add Feedback</button>
          </form>
        </div>
      </div>
    </div>
  );
}

export default AddFacultyFeedback;
