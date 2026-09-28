const state = { token: localStorage.getItem('lmsToken'), user: null };
const $ = (id) => document.getElementById(id);

function toast(message) {
  const el = $('toast');
  el.textContent = message;
  el.classList.add('show');
  setTimeout(() => el.classList.remove('show'), 2400);
}

async function api(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (state.token) headers.Authorization = `Bearer ${state.token}`;
  const response = await fetch(path, { ...options, headers });
  let data = null;
  try { data = await response.json(); } catch (_) {}
  if (!response.ok) throw new Error(data?.message || `Request failed: ${response.status}`);
  return data;
}

function showApp() {
  $('authSection').classList.add('hidden');
  $('appSection').classList.remove('hidden');
  $('logoutBtn').classList.remove('hidden');
  const instructor = state.user.role === 'INSTRUCTOR' || state.user.role === 'ADMIN';
  $('creatorPanel').classList.toggle('hidden', !instructor);
  $('studentPanel').classList.toggle('hidden', state.user.role !== 'STUDENT');
  $('roleLabel').textContent = state.user.role;
}

async function login(email, password) {
  const data = await api('/api/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password })
  });
  state.token = data.token;
  state.user = data;
  localStorage.setItem('lmsToken', data.token);
  showApp();
  await loadDashboard();
  await loadCourses();
  if (state.user.role === 'STUDENT') await loadEnrollments();
}

async function loadDashboard() {
  const data = await api('/api/dashboard');
  $('courseCount').textContent = data.courses;
  $('studentCount').textContent = data.students;
  $('enrollmentCount').textContent = data.enrollments;
}

async function loadCourses() {
  const canCreate = state.user?.role === 'INSTRUCTOR' || state.user?.role === 'ADMIN';
  const query = $('courseSearch')?.value?.trim();
  const endpoint = query ? `/api/courses?query=${encodeURIComponent(query)}` : '/api/courses';
  const courses = await api(endpoint);
  const container = $('courseList');
  container.innerHTML = '';
  courses.forEach(course => {
    const card = document.createElement('article');
    card.className = 'course';
    card.innerHTML = `
      <div class="meta">${escapeHtml(course.category)} · Instructor: ${escapeHtml(course.instructor?.name || '—')}</div>
      <h3>${escapeHtml(course.title)}</h3>
      <p>${escapeHtml(course.description)}</p>
      <div class="course-actions">
        <button data-id="${course.id}" class="viewLessons">View lessons</button>
        ${state.user.role === 'STUDENT' ? `<button data-id="${course.id}" class="secondary enrollBtn">Enroll</button>` : ''}
        ${canCreate ? `<button data-id="${course.id}" class="secondary addLessonBtn">Add lesson</button>` : ''}
      </div>
      <div class="lessonBox" id="lessons-${course.id}"></div>`;
    container.appendChild(card);
  });

  document.querySelectorAll('.viewLessons').forEach(btn => btn.addEventListener('click', () => loadLessons(btn.dataset.id)));
  document.querySelectorAll('.enrollBtn').forEach(btn => btn.addEventListener('click', () => enroll(btn.dataset.id)));
  document.querySelectorAll('.addLessonBtn').forEach(btn => btn.addEventListener('click', () => addLesson(btn.dataset.id)));
}

async function loadLessons(courseId) {
  const box = $(`lessons-${courseId}`);
  const lessons = await api(`/api/courses/${courseId}/lessons`);
  if (!lessons.length) {
    box.innerHTML = '<p class="muted">No lessons yet.</p>';
    return;
  }
  box.innerHTML = `<div class="meta">Lessons</div>` + lessons.map(lesson => {
    const embed = lesson.videoUrl ? getYouTubeEmbedUrl(lesson.videoUrl) : null;
    const videoMarkup = embed ? `
      <div class="video-wrap">
        <iframe src="${embed}" title="${escapeHtml(lesson.title)} video" loading="lazy" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
      </div>` : (lesson.videoUrl ? `<a class="video-link" href="${escapeHtml(lesson.videoUrl)}" target="_blank" rel="noopener">Watch lesson video ↗</a>` : '');
    return `
    <div class="lesson" style="padding:16px 0; border-top:1px solid #edf1f7">
      <strong>${lesson.lessonOrder}. ${escapeHtml(lesson.title)}</strong>
      <div class="muted">${escapeHtml(lesson.content || '')}</div>
      ${videoMarkup}
      ${state.user.role === 'STUDENT' ? `<button class="secondary completeBtn" data-id="${lesson.id}" style="margin-top:8px">Mark complete</button>` : ''}
    </div>`;
  }).join('');
  box.querySelectorAll('.completeBtn').forEach(btn => btn.addEventListener('click', () => completeLesson(btn.dataset.id)));
}

async function addLesson(courseId) {
  const title = window.prompt('Lesson title:');
  if (!title) return;
  const order = Number(window.prompt('Lesson order:', '1'));
  if (!Number.isInteger(order) || order < 1) return toast('Lesson order must be a positive number');
  const videoUrl = window.prompt('YouTube video URL (optional):', '');
  const content = window.prompt('Lesson description (optional):', '');
  try {
    await api(`/api/courses/${courseId}/lessons`, {
      method: 'POST',
      body: JSON.stringify({ title, content, videoUrl, lessonOrder: order })
    });
    toast('Lesson created');
  } catch (e) { toast(e.message); }
}

async function enroll(courseId) {
  try {
    await api(`/api/enrollments/courses/${courseId}`, { method: 'POST' });
    toast('Enrolled successfully');
    await loadEnrollments();
  } catch (e) { toast(e.message); }
}

async function loadEnrollments() {
  const enrollments = await api('/api/enrollments/me');
  $('enrollmentList').innerHTML = enrollments.length ? enrollments.map(e => `
    <article class="course">
      <div class="meta">${escapeHtml(e.course.category)}</div>
      <h3>${escapeHtml(e.course.title)}</h3>
      <p>${e.progressPercent}% complete</p>
      <div style="height:8px;background:#e8edf6;border-radius:999px;overflow:hidden"><div style="width:${e.progressPercent}%;height:100%;background:#315bea"></div></div>
    </article>`).join('') : '<p class="muted">No enrollments yet.</p>';
}

async function completeLesson(lessonId) {
  try {
    await api(`/api/progress/lessons/${lessonId}/complete`, { method: 'POST' });
    toast('Lesson marked complete');
    await loadEnrollments();
  } catch (e) { toast(e.message); }
}

function getYouTubeEmbedUrl(url) {
  try {
    const parsed = new URL(url);
    if (parsed.hostname.includes('youtube.com')) {
      const id = parsed.searchParams.get('v');
      if (id) return `https://www.youtube.com/embed/${id}?rel=0&modestbranding=1${parsed.searchParams.get('t') ? `&start=${parseTime(parsed.searchParams.get('t'))}` : ''}`;
    }
    if (parsed.hostname === 'youtu.be') {
      const id = parsed.pathname.slice(1);
      if (id) return `https://www.youtube.com/embed/${id}?rel=0&modestbranding=1`;
    }
  } catch (_) {}
  return null;
}

function parseTime(value) {
  if (!value) return 0;
  const match = String(value).match(/(?:(\d+)h)?(?:(\d+)m)?(?:(\d+)s?)?/i);
  if (!match) return Number.parseInt(value, 10) || 0;
  return (Number(match[1] || 0) * 3600) + (Number(match[2] || 0) * 60) + Number(match[3] || 0);
}

function debounce(fn, delay) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => fn(...args), delay);
  };
}

function escapeHtml(value) {
  return String(value ?? '').replace(/[&<>"']/g, ch => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[ch]));
}

$('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  try {
    $('authMessage').textContent = '';
    await login($('loginEmail').value, $('loginPassword').value);
  } catch (err) { $('authMessage').textContent = err.message; }
});

$('registerForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  try {
    const data = await api('/api/auth/register', {
      method: 'POST',
      body: JSON.stringify({
        name: $('registerName').value,
        email: $('registerEmail').value,
        password: $('registerPassword').value
      })
    });
    state.token = data.token;
    state.user = data;
    localStorage.setItem('lmsToken', data.token);
    showApp();
    await loadDashboard();
    await loadCourses();
    await loadEnrollments();
  } catch (err) { $('authMessage').textContent = err.message; }
});

$('courseForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  try {
    await api('/api/courses', {
      method: 'POST',
      body: JSON.stringify({
        title: $('courseTitle').value,
        category: $('courseCategory').value,
        description: $('courseDescription').value
      })
    });
    e.target.reset();
    toast('Course created');
    await loadDashboard();
    await loadCourses();
  } catch (err) { toast(err.message); }
});

$('courseSearch')?.addEventListener('input', debounce(loadCourses, 260));

$('refreshBtn').addEventListener('click', async () => {
  await loadDashboard();
  await loadCourses();
  if (state.user.role === 'STUDENT') await loadEnrollments();
});

$('logoutBtn').addEventListener('click', () => {
  localStorage.removeItem('lmsToken');
  state.token = null;
  state.user = null;
  location.reload();
});

(async function boot() {
  if (!state.token) return;
  try {
    const data = await api('/api/dashboard');
    state.user = data.user;
    showApp();
    await loadDashboard();
    await loadCourses();
    if (state.user.role === 'STUDENT') await loadEnrollments();
  } catch (_) {
    localStorage.removeItem('lmsToken');
    state.token = null;
  }
})();
