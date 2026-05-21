// ==========================================================================
// 1. ITZULPENEN HIZTEGIA (KATALOGO-ORRIA)
// ==========================================================================
const dictCatalog = {
  "EU": {
    "nav_back": "Itzuli hasierara",
    "cat_title": "Inbentario Osoa",
    "cat_sub": "Hemen aurkituko dituzu Bermeoko Udalaren biltegian dauden objektu guztiak. Zure bat ikusten baduzu, sakatu erreklamatzeko botoia.",
    "cat_search_place": "Bilatu objektuaren izena...",
    "cat_all_opt": "Kategoria guztiak (Todas)",
    "cat_h2_results": "Katalogoaren emaitzak",
    "cat_loading": "Datuak kargatzen... / Cargando base de datos...",
    "cat_no_res": "Ez da aurkitu emaitzarik bilaketa honekin. / No se han encontrado resultados.",
    "cat_load_more": "Gehiago ikusi / Ver más",
    "btn_claim_card": "Mezu bat bidali",
    "foot_desc": "Bermeoko Udaleko galdu eta aurkituen zerbitzu ofiziala. Herritarrei zerbitzuan, gardentasunez eta eraginkortasunez.", 
    "foot_col1": "Zerbitzuak", 
    "foot_link_inv": "Inbentarioa bilatu", 
    "foot_link_claim": "Erreklamazioa hasi", 
    "foot_link_my": "Nire erreklamazioak", 
    "foot_link_not": "Jakinarazpenak", 
    "foot_link_faq": "Galdera ohikoak", 
    "foot_col2": "Udala", 
    "foot_link_town": "Bermeoko Udala", 
    "foot_link_serv": "Zerbitzu guztiak", 
    "foot_link_board": "Iragarki-taula", 
    "foot_link_press": "Prentsa-kabineta", 
    "foot_link_contact": "Kontaktua", 
    "foot_col3": "Legala", 
    "foot_link_priv": "Pribatutasun-politika", 
    "foot_link_terms": "Erabileraren baldintzak", 
    "foot_link_cook": "Cookie politika", 
    "foot_link_acc": "Irisgarritasuna", 
    "foot_legal_text": "v1.0 · © 2026 Bermeoko Udala · Eskubide guztiak erreserbatuta · Eder Martin Mosquerak eta Yeray Garrido Parrak maitasun eta kafeina handiz egina."
  },
  "ES": {
    "nav_back": "Volver al inicio",
    "cat_title": "Inventario Completo",
    "cat_sub": "Aquí encontrarás todos los objetos almacenados por el Ayuntamiento de Bermeo. Si reconoces el tuyo, pulsa en el botón para reclamarlo.",
    "cat_search_place": "Buscar nombre del objeto...",
    "cat_all_opt": "Todas las categorías",
    "cat_h2_results": "Resultados del catálogo",
    "cat_loading": "Cargando datos...",
    "cat_no_res": "No se han encontrado resultados para esta búsqueda.",
    "cat_load_more": "Ver más",
    "btn_claim_card": "Enviar mensaje",
    "foot_desc": "Servicio oficial de objetos perdidos del Ayuntamiento de Bermeo. Al servicio de los ciudadanos con transparencia y eficacia.", 
    "foot_col1": "Servicios", 
    "foot_link_inv": "Buscar en inventario", 
    "foot_link_claim": "Iniciar reclamación", 
    "foot_link_my": "Mis reclamaciones", 
    "foot_link_not": "Notificaciones", 
    "foot_link_faq": "Preguntas frecuentes", 
    "foot_col2": "Ayuntamiento", 
    "foot_link_town": "Ayuntamiento de Bermeo", 
    "foot_link_serv": "Todos los servicios", 
    "foot_link_board": "Tablón de anuncios", 
    "foot_link_press": "Gabinete de prensa", 
    "foot_link_contact": "Contacto", 
    "foot_col3": "Legal", 
    "foot_link_priv": "Política de privacidad", 
    "foot_link_terms": "Condiciones de uso", 
    "foot_link_cook": "Política de cookies", 
    "foot_link_acc": "Accesibilidad", 
    "foot_legal_text": "v1.0 · © 2026 Ayuntamiento de Bermeo · Todos los derechos reservados · Hecho con mucho amor y cafeína por Eder Martin Mosquera y Yeray Garrido Parra."
  }
};

// ==========================================================================
// 2. HIZKUNTZA ETA GAIA ALDATZEKO FUNTZIOA
// ==========================================================================
function setLang(idioma) {
  localStorage.setItem('appLang', idioma);
  document.querySelectorAll('.lang-btn').forEach(btn => {
    const isActive = btn.textContent === idioma;
    btn.classList.toggle('active', isActive);
    btn.setAttribute('aria-pressed', isActive);
  });
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const clave = el.getAttribute('data-i18n');
    if (dictCatalog[idioma] && dictCatalog[idioma][clave]) {
      if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
        el.placeholder = dictCatalog[idioma][clave];
      } else {
        el.textContent = dictCatalog[idioma][clave];
      }
    }
  });
  
  document.querySelectorAll('.btn-claim').forEach(btn => {
    btn.textContent = dictCatalog[idioma]["btn_claim_card"];
  });
}

function initTheme() {
  const themeToggle = document.getElementById('themeToggle');
  const moonIcon = document.getElementById('moonIcon');
  const sunIcon = document.getElementById('sunIcon');
  const isDark = localStorage.getItem('appTheme') === 'dark';
  
  if (isDark) {
    document.body.classList.add('dark-theme');
    if (moonIcon && sunIcon) {
      moonIcon.style.display = 'none';
      sunIcon.style.display = 'block';
    }
  }

  if (themeToggle) {
    themeToggle.addEventListener('click', () => {
      document.body.classList.toggle('dark-theme');
      const currentTheme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
      localStorage.setItem('appTheme', currentTheme);
      if (currentTheme === 'dark') {
        moonIcon.style.display = 'none';
        sunIcon.style.display = 'block';
      } else {
        moonIcon.style.display = 'block';
        sunIcon.style.display = 'none';
      }
    });
  }
}

// ==========================================================================
// 3. LOGIKA NAGUSIA (XML ETA GERTAERAK)
// ==========================================================================
document.addEventListener("DOMContentLoaded", () => {
  // Hizkuntza eta gaia hasieratu
  setLang(localStorage.getItem('appLang') || 'EU');
  initTheme();

  const catalogGrid = document.getElementById('catalogGrid');
  const searchInput = document.getElementById('searchInput');
  const categorySelect = document.getElementById('categorySelect');
  const noResultsMsg = document.getElementById('noResults');
  const loadingMsg = document.getElementById('loadingMsg');
  const loadMoreContainer = document.getElementById('loadMoreContainer');
  const btnLoadMore = document.getElementById('btnLoadMore');
  
  let cards = []; 
  let visibleLimit = 6; 

  if (categorySelect) {
    fetch('/datuak/artikuluak.xml')
      .then(response => {
        if (!response.ok) throw new Error("Ezin izan da XML artikuluak fitxategia kargatu");
        return response.text();
      })
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const artikuluak = xmlDoc.querySelectorAll("artikulua");
        const uniqueKats = new Set();
        
        artikuluak.forEach(art => {
          const kat = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : 'Bestelakoak';
          uniqueKats.add(kat);
        });

        uniqueKats.forEach(izena => {
          const option = document.createElement('option');
          option.value = izena.toLowerCase(); 
          option.textContent = izena; 
          categorySelect.appendChild(option);
        });
      })
      .catch(error => console.error("Errorea kategoriak kargatzean:", error));
  }

  if (catalogGrid) {
    fetch('/datuak/artikuluak.xml')
      .then(response => {
        if (!response.ok) throw new Error("Ezin izan da XML artikuluak fitxategia kargatu");
        return response.text();
      })
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const artikuluak = xmlDoc.querySelectorAll("artikulua");
        catalogGrid.innerHTML = ''; 

        artikuluak.forEach(art => {
          const id = art.querySelector("id") ? art.querySelector("id").textContent : '?';
          const izena = art.querySelector("izena") ? art.querySelector("izena").textContent : 'Izen gabea';
          const kategoria = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : 'Bestelakoak';
          const deskribapena = art.querySelector("deskribapena") ? art.querySelector("deskribapena").textContent : '';
          const sarreraData = art.querySelector("sarreraData") ? art.querySelector("sarreraData").textContent.trim() : '';
          const argazkia = art.querySelector("argazkia") ? art.querySelector("argazkia").textContent.trim() : '';
          
          let visualContent = '';
          if (argazkia !== '') {
            visualContent = `<img src="/irudiak/${argazkia}" alt="${izena}" class="item-img-cover">`;
          } else {
            visualContent = `
              <div class="img-placeholder">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>
                <span class="img-placeholder-text">Argazkirik gabe</span>
              </div>`;
          }

          const card = document.createElement('div');
          card.className = 'item-card';
          card.setAttribute('data-category', kategoria);
          card.classList.add('d-none'); 
          
          let dataHTML = '';
          if (sarreraData !== '') {
            dataHTML = `<span class="item-date">Aurkituta: ${sarreraData}</span>`;
          }

          card.innerHTML = `
            <div class="item-img">${visualContent}</div>
            <div class="item-info">
              <span class="item-cat-badge">${kategoria.toUpperCase()}</span>
              <h3 class="item-name item-name-spaced">${izena}</h3>
              ${dataHTML}
              ${deskribapena ? `<p class="item-desc">${deskribapena}</p>` : ''}
              <a href="mailto:galduaurkituak@bermeo.eus?subject=Erreklamazioa:%20${encodeURIComponent(izena)}%20(ID:%20${id})" class="btn-claim mt-auto">Mezu bat bidali</a>
            </div>
          `;
          catalogGrid.appendChild(card);
        });

        cards = Array.from(document.querySelectorAll('.item-card'));
        
        // Botoiak itzuli DOM-ean sortu bezain laster
        const currentLang = localStorage.getItem('appLang') || 'EU';
        document.querySelectorAll('.btn-claim').forEach(btn => {
          btn.textContent = dictCatalog[currentLang]["btn_claim_card"];
        });

        filterItems(); 
      })
      .catch(error => {
        console.error("Errorea:", error);
        if (loadingMsg) {
           loadingMsg.textContent = "Errorea datuak kargatzean.";
        }
      });
  }

  function filterItems() {
    if (!searchInput || !categorySelect) return;
    const searchTerm = searchInput.value.toLowerCase();
    const selectedCategory = categorySelect.value;

    let matchedCards = cards.filter(card => {
      const itemName = card.querySelector('.item-name').textContent.toLowerCase();
      const itemCategory = card.getAttribute('data-category');
      const matchesSearch = itemName.includes(searchTerm);
      const matchesCategory = selectedCategory === 'all' || (itemCategory && itemCategory.toLowerCase() === selectedCategory.toLowerCase());
      return matchesSearch && matchesCategory;
    });

    cards.forEach(card => card.classList.add('d-none'));

    for (let i = 0; i < matchedCards.length; i++) {
      if (i < visibleLimit) {
        matchedCards[i].classList.remove('d-none');
      }
    }

    if (matchedCards.length === 0 && cards.length > 0) {
      if(noResultsMsg) noResultsMsg.classList.remove('d-none');
      if(loadMoreContainer) loadMoreContainer.classList.add('d-none');
    } else {
      if(noResultsMsg) noResultsMsg.classList.add('d-none');
      if (matchedCards.length > visibleLimit) {
        if(loadMoreContainer) loadMoreContainer.classList.remove('d-none');
      } else {
        if(loadMoreContainer) loadMoreContainer.classList.add('d-none');
      }
    }
  }

  function onFilterChange() {
    visibleLimit = 6; 
    filterItems();
  }

  if (searchInput) searchInput.addEventListener('input', onFilterChange);
  if (categorySelect) categorySelect.addEventListener('change', onFilterChange);
  if (btnLoadMore) {
    btnLoadMore.addEventListener('click', () => {
      visibleLimit += 1000; 
      filterItems(); 
    });
  }
});