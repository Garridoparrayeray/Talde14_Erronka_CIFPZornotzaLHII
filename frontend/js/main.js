const navbar = document.getElementById('navbar');
const navLinks = document.querySelectorAll('.nav-links a');

const sectionsToTrack = [
  { id: '#', element: document.querySelector('.hero') },
  { id: '#claim', element: document.getElementById('claim') },
  { id: '#process', element: document.getElementById('process') },
  { id: '#categories', element: document.getElementById('categories') },
  { id: '#contact', element: document.getElementById('contact') }
];

window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 20);

  let current = '#'; 
  sectionsToTrack.forEach(sec => {
    if (sec.element) {
      const sectionTop = sec.element.offsetTop;
      if (window.scrollY >= sectionTop - 200) {
        current = sec.id;
      }
    }
  });

  if ((window.innerHeight + Math.round(window.scrollY)) >= document.body.offsetHeight - 50) {
    current = '#contact';
  }

  navLinks.forEach(a => {
    a.classList.remove('active');
    if (a.getAttribute('href') === current) {
      a.classList.add('active');
    }
  });
});

function setLang(lang) {
  document.querySelectorAll('.lang-btn').forEach(btn => {
    btn.classList.toggle('active', btn.textContent === lang);
    btn.setAttribute('aria-pressed', btn.textContent === lang);
  });
}

document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function(e) {
    const targetId = this.getAttribute('href');
    if (targetId === '#') {
      e.preventDefault();
      window.scrollTo({ top: 0, behavior: 'smooth' });
      return;
    }
    const target = document.querySelector(targetId);
    if (target) { 
      e.preventDefault(); 
      target.scrollIntoView({ behavior: 'smooth' }); 
    }
  });
});

const fileInput = document.getElementById('claim-file');
const removeFileBtn = document.getElementById('remove-file-btn');

if (fileInput && removeFileBtn) {
  fileInput.addEventListener('change', function() {
    if (this.files && this.files.length > 0) {
      removeFileBtn.style.display = 'inline-flex';
    } else {
      removeFileBtn.style.display = 'none';
    }
  });

  removeFileBtn.addEventListener('click', function() {
    fileInput.value = '';
    removeFileBtn.style.display = 'none';
  });
}

// --- LÓGICA MENÚ MÓVIL ---
const menuToggle = document.getElementById('menuToggle');
const mobileNav = document.getElementById('mobileNav');
const mobileLinks = document.querySelectorAll('.mobile-nav a');

menuToggle.addEventListener('click', () => {
  const isVisible = mobileNav.style.display === 'flex';
  mobileNav.style.display = isVisible ? 'none' : 'flex';
});

// Cerrar el menú al hacer clic en cualquier enlace
mobileLinks.forEach(link => {
  link.addEventListener('click', () => {
    mobileNav.style.display = 'none';
  });
});
// --- LÓGICA PARA CARGAR CATEGORÍAS EN PORTADA ---
document.addEventListener("DOMContentLoaded", () => {
  const categoryGrid = document.getElementById('categoryGrid');
  
  if (categoryGrid) {
    // Diccionario de iconos y colores según el ID de la categoría
    const katEstiloak = {
      "jantziak": { bg: "#EEF2FF", color: "#3B5BDB", svg: '<rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2"/>' },
      "gakoak": { bg: "#FFF3D4", color: "#E09A10", svg: '<path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/>' },
      "elektronika": { bg: "#E4F9ED", color: "#2BB673", svg: '<rect x="5" y="2" width="14" height="20" rx="2" ry="2"/><line x1="12" y1="18" x2="12.01" y2="18"/>' },
      "dokumentuak": { bg: "#FDE8E8", color: "#E84040", svg: '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14,2 14,8 20,8"/>' },
      "eguzkitakoak": { bg: "#F0EDFF", color: "#7B5FDC", svg: '<circle cx="12" cy="12" r="1"/><circle cx="19" cy="12" r="1"/><circle cx="5" cy="12" r="1"/>' },
      "bestelakoak": { bg: "#E6F4F1", color: "#0D9488", svg: '<line x1="12" y1="2" x2="12" y2="6"/><path d="M6.3 6.3l-2.8-2.8M17.7 6.3l2.8-2.8M6 12H2M22 12h-4M6.3 17.7l-2.8 2.8M17.7 17.7l2.8 2.8M12 18v4"/><circle cx="12" cy="12" r="4"/>' }
    };

    fetch('datuak/kategoriak.xml')
      .then(response => response.text())
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const kategoriak = xmlDoc.querySelectorAll("kategoria");
        categoryGrid.innerHTML = ''; // Limpiamos el texto de carga

        kategoriak.forEach(kat => {
          const id = kat.getAttribute("id");
          const izena = kat.querySelector("izena").textContent;
          const kopurua = kat.querySelector("kopurua").textContent;
          
          // Si la categoría no está en nuestro diccionario, usamos gris por defecto
          const estilo = katEstiloak[id] || { bg: "#F2F5FD", color: "#6B6F80", svg: '<circle cx="12" cy="12" r="10"/><path d="M12 8v4l3 3"/>' };

          const card = document.createElement('a');
          card.href = 'html/objektu-zerrenda.html';
          card.className = 'cat-card';
          card.innerHTML = `
            <div class="cat-icon" style="background:${estilo.bg};">
              <svg viewBox="0 0 24 24" style="stroke:${estilo.color};"><g>${estilo.svg}</g></svg>
            </div>
            <div class="cat-name">${izena}</div>
            <div class="cat-count">${kopurua} objektu</div>
          `;
          categoryGrid.appendChild(card);
        });
      })
      .catch(error => {
        console.error("Errorea:", error);
        categoryGrid.innerHTML = '<p style="color: red;">Errorea datuak kargatzean.</p>';
      });
  }
});