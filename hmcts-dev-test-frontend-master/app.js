const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');
const fetch = require('node-fetch');
const { body, validationResult } = require('express-validator');
require('dotenv').config();

const app = express();
const BACKEND = process.env.BACKEND_URL || 'http://localhost:4000';
const PORT = process.env.PORT || 3000;

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Helper: call backend
async function backendFetch(endpoint, opts = {}) {
  const url = `${BACKEND}${endpoint}`;
  console.log('Fetching', url, opts);
  try {
    const response = await fetch(url, opts);
    console.log('Response', response.status);
    const text = await response.text();
    console.log('Raw body:', text);
    let data;
    try { data = JSON.parse(text); } catch (e) { data = text; }
    if (!response.ok) {
      const err = new Error(`Backend error ${response.status}`);
      err.status = response.status;
      err.body = data;
      throw err;
    }
    return data;
  } catch (err) {
    console.error('Fetch error:', err);
    throw err;
  }
}


// Routes
app.get('/', async (req, res) => {
  try {
    const tasks = await backendFetch('/tasks');
    console.log(tasks);
    res.render('index', { tasks });
  } catch (err) {
    res.status(500).render('error', { message: 'Unable to load tasks', error: err });
  }
});

app.get('/tasks/new', (req, res) => {
  res.render('new', { errors: [], values: {} });
});

app.post('/tasks', [
  body('title').trim().notEmpty().withMessage('Title is required'),
  body('status').trim().notEmpty().withMessage('Status is required')
], async (req, res) => {
  const errors = validationResult(req);
  const values = req.body;
  if (!errors.isEmpty()) {
    return res.status(400).render('new', { errors: errors.array(), values });
  }
  try {
    const payload = {
      title: values.title,
      description: values.description || undefined,
      status: values.status,
      dueDate: values.dueDate || undefined
    };
    await backendFetch('/tasks', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });
    res.redirect('/');
  } catch (err) {
    res.status(500).render('error', { message: 'Failed to create task', error: err });
  }
});

app.get('/tasks/:id', async (req, res) => {
  try {
    const task = await backendFetch(`/tasks/${req.params.id}`);
    res.render('task', { task });
  } catch (err) {
    res.status(404).render('error', { message: 'Task not found', error: err });
  }
});

app.get('/tasks/:id/edit', async (req, res) => {
  try {
    const task = await backendFetch(`/tasks/${req.params.id}`);
    res.render('edit', { errors: [], values: task });
  } catch (err) {
    res.status(404).render('error', { message: 'Task not found', error: err });
  }
});

app.post('/tasks/:id/edit', [
  body('title').trim().notEmpty().withMessage('Title is required'),
  body('status').trim().notEmpty().withMessage('Status is required')
], async (req, res) => {
  const errors = validationResult(req);
  const values = req.body;
  const id = req.params.id;
  if (!errors.isEmpty()) {
    return res.status(400).render('edit', { errors: errors.array(), values });
  }
  try {
    await backendFetch(`/tasks/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(values)
    });
    res.redirect(`/tasks/${id}`);
  } catch (err) {
    res.status(500).render('error', { message: 'Failed to update task', error: err });
  }
});

app.post('/tasks/:id/status', async (req, res) => {
  const id = req.params.id;
  const { status } = req.body;
  try {
    await backendFetch(`/tasks/${id}/status`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ status })
    });
    res.redirect(`/tasks/${id}`);
  } catch (err) {
    res.status(500).render('error', { message: 'Failed to update status', error: err });
  }
});

app.post('/tasks/:id/delete', async (req, res) => {
  try {
    await backendFetch(`/tasks/${req.params.id}`, { method: 'DELETE' });
    res.redirect('/');
  } catch (err) {
    res.status(500).render('error', { message: 'Failed to delete task', error: err });
  }
});

// Error view for general use
app.use((req, res) => {
  res.status(404).render('error', { message: 'Not found', error: {} });
});

if (require.main === module) {
  app.listen(PORT, () => console.log(`Frontend listening on http://localhost:${PORT}`));
}

app.get('/tasks/new', (req, res) => {
  res.render('new', { errors: [], values: {} });
});
module.exports = app;
