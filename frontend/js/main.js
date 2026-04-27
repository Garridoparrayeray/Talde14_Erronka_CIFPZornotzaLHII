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
// --- LÓGICA PARA CARGAR DATOS DESDE EL XML ---
document.addEventListener("DOMContentLoaded", () => {
  const isIndexPage = document.getElementById('kat-jantziak');
  
  if (isIndexPage) {
    fetch('datuak/kategoriak.xml')
      .then(response => {
        if (!response.ok) throw new Error("Ezin izan da XML fitxategia kargatu");
        return response.text();
      })
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        // Obtenemos todas las etiquetas <kategoria>
        const kategoriak = xmlDoc.querySelectorAll("kategoria");
        
        // Recorremos cada una
        kategoriak.forEach(kat => {
          const id = kat.getAttribute("id"); 
          const kopurua = kat.querySelector("kopurua").textContent;
          
          // Buscamos la caja en el HTML y le metemos el número
          const htmlElement = document.getElementById(`kat-${id}`);
          if (htmlElement) {
            htmlElement.textContent = `${kopurua} objektu`;
          }
        });
      })
      .catch(error => {
        console.error("Errorea:", error);
        // Si hay error (ej: el xml no existe aún), mostramos un texto por defecto
        document.querySelectorAll('.cat-count').forEach(el => el.textContent = "Datu barik");
      });
  }
});