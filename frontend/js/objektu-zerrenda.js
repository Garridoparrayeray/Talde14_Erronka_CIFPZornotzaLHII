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
    "foot_desc": "Bermeoko Udaleko galdu eta aurkituen zerbitzu ofiziala. Herritarrei zerbitzuan, gardentasunez eta eraginkortasunez.", "foot_col1": "Zerbitzuak", "foot_link_inv": "Inbentarioa bilatu", "foot_link_claim": "Erreklamazioa hasi", "foot_link_my": "Nire erreklamazioak", "foot_link_not": "Jakinarazpenak", "foot_link_faq": "Galdera ohikoak", "foot_col2": "Udala", "foot_link_town": "Bermeoko Udala", "foot_link_serv": "Zerbitzu guztiak", "foot_link_board": "Iragarki-taula", "foot_link_press": "Prentsa-kabineta", "foot_link_contact": "Contacto", "foot_col3": "Legala", "foot_link_priv": "Pribatutasun-politika", "foot_link_terms": "Erabileraren baldintzak", "foot_link_cook": "Cookie politika", "foot_link_acc": "Irisgarritasuna", "foot_legal_text": "v1.0 · © 2026 Bermeoko Udala · Eskubide guztiak erreserbatuta"
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
    "foot_desc": "Servicio oficial de objetos perdidos del Ayuntamiento de Bermeo. Al servicio de los ciudadanos con transparencia y eficacia.", "foot_col1": "Servicios", "foot_link_inv": "Buscar en inventario", "foot_link_claim": "Iniciar reclamación", "foot_link_my": "Mis reclamaciones", "foot_link_not": "Notificaciones", "foot_link_faq": "Preguntas frecuentes", "foot_col2": "Ayuntamiento", "foot_link_town": "Ayuntamiento de Bermeo", "foot_link_serv": "Todos los servicios", "foot_link_board": "Tablón de anuncios", "foot_link_press": "Gabinete de prensa", "foot_link_contact": "Contacto", "foot_col3": "Legal", "foot_link_priv": "Política de privacidad", "foot_link_terms": "Condiciones de uso", "foot_link_cook": "Política de cookies", "foot_link_acc": "Accesibilidad", "foot_legal_text": "v1.0 · © 2026 Ayuntamiento de Bermeo · Todos los derechos reservados"
  }
};

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
  
  // Traducir los botones inyectados desde XML en el catálogo
  document.querySelectorAll('.btn-claim').forEach(btn => {
    btn.textContent = dictCatalog[idioma]["btn_claim_card"];
  });
}

document.addEventListener('DOMContentLoaded', () => {
  setLang(localStorage.getItem('appLang') || 'EU');
});
document.addEventListener("DOMContentLoaded", () => {
  // 1. VARIABLES DEL DOM
  const catalogGrid = document.getElementById('catalogGrid');
  const searchInput = document.getElementById('searchInput');
  const categorySelect = document.getElementById('categorySelect');
  const noResultsMsg = document.getElementById('noResults');
  const loadingMsg = document.getElementById('loadingMsg');
  
  const loadMoreContainer = document.getElementById('loadMoreContainer');
  const btnLoadMore = document.getElementById('btnLoadMore');
  
  let cards = []; 
  let visibleLimit = 6; 

  // 2. CARGAR EL DESPLEGABLE DE CATEGORÍAS (Desde kategoriak.xml)
  if (categorySelect) {
    fetch('../datuak/kategoriak.xml')
      .then(response => {
        if (!response.ok) throw new Error("XML kategoria fitxategia ez da aurkitu");
        return response.text();
      })
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const kategoriak = xmlDoc.querySelectorAll("kategoria");
        kategoriak.forEach(kat => {
          const izena = kat.querySelector("izena").textContent;
          const option = document.createElement('option');
          option.value = izena; 
          option.textContent = izena; 
          categorySelect.appendChild(option);
        });
      })
      .catch(error => console.error("Errorea kategoriak kargatzean:", error));
  }

  // 3. CARGAR LAS TARJETAS DE OBJETOS (Desde artikuluak.xml)
  if (catalogGrid) {
    fetch('../datuak/artikuluak.xml')
      .then(response => {
        if (!response.ok) throw new Error("Ezin izan da XML artikuluak fitxategia kargatu");
        return response.text();
      })
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const artikuluak = xmlDoc.querySelectorAll("artikulua");
        catalogGrid.innerHTML = ''; // Limpiar "Kargatzen..."

        // CREAR LAS TARJETAS DINÁMICAMENTE
        artikuluak.forEach(art => {
          // Extraer datos con el NUEVO FORMATO XML
          const id = art.querySelector("id") ? art.querySelector("id").textContent : '?';
          const izena = art.querySelector("izena") ? art.querySelector("izena").textContent : 'Izen gabea';
          const kategoria = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : 'Bestelakoak';
          const deskribapena = art.querySelector("deskribapena") ? art.querySelector("deskribapena").textContent : '';
          const sarreraData = art.querySelector("sarreraData") ? art.querySelector("sarreraData").textContent.trim() : '';
          const argazkia = art.querySelector("argazkia") ? art.querySelector("argazkia").textContent.trim() : '';
          
          // Lógica de la imagen
          let visualContent = '';
          if (argazkia !== '') {
            visualContent = `<img src="../datuak/img/${argazkia}" alt="${izena}" style="width:100%; height:100%; object-fit:cover;">`;
          } else {
            visualContent = `
              <div class="img-placeholder">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>
                <span style="font-size: 11px;">Argazkirik gabe</span>
              </div>`;
          }

          // Crear la tarjeta HTML
          const card = document.createElement('div');
          card.className = 'item-card';
          card.setAttribute('data-category', kategoria);
          card.style.display = 'none'; // Se crean ocultas
          
          // Si hay fecha de entrada, preparamos un HTML pequeñito para mostrarlo
          let dataHTML = '';
          if (sarreraData !== '') {
            dataHTML = `<span style="font-size: 11px; color: var(--gray-500); display: block; margin-bottom: 8px;">Aurkituta: ${sarreraData}</span>`;
          }

          card.innerHTML = `
            <div class="item-img">${visualContent}</div>
            <div class="item-info">
              <span class="item-cat-badge">${kategoria.toUpperCase()}</span>
              <h3 class="item-name" style="margin-bottom: 8px; min-height: 24px;">${izena}</h3>
              ${dataHTML}
              ${deskribapena ? `<p style="font-size: 13px; color: var(--gray-500); margin-bottom: 16px; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden;">${deskribapena}</p>` : ''}
              <a href="mailto:galduaurkituak@bermeo.eus?subject=Erreklamazioa:%20${encodeURIComponent(izena)}%20(ID:%20${id})" class="btn-claim" style="margin-top: auto;">Mezu bat bidali</a>
            </div>
          `;
          catalogGrid.appendChild(card);
        });

        cards = Array.from(document.querySelectorAll('.item-card'));
        filterItems(); 
      })
      .catch(error => {
        console.error("Errorea:", error);
        if (loadingMsg) {
           loadingMsg.textContent = "Errorea datuak kargatzean. Egiaztatu zerbitzari lokala erabiltzen ari zarela (Live Server).";
        }
      });
  }

  // 4. LÓGICA DE FILTRADO Y LÍMITES
  function filterItems() {
    if (!searchInput || !categorySelect) return;

    const searchTerm = searchInput.value.toLowerCase();
    const selectedCategory = categorySelect.value;

    let matchedCards = cards.filter(card => {
      const itemName = card.querySelector('.item-name').textContent.toLowerCase();
      const itemCategory = card.getAttribute('data-category');
      
      const matchesSearch = itemName.includes(searchTerm);
      const matchesCategory = selectedCategory === 'all' || itemCategory === selectedCategory;
      
      return matchesSearch && matchesCategory;
    });

    cards.forEach(card => card.style.display = 'none');

    for (let i = 0; i < matchedCards.length; i++) {
      if (i < visibleLimit) {
        matchedCards[i].style.display = 'flex';
      }
    }

    if (matchedCards.length === 0 && cards.length > 0) {
      if(noResultsMsg) noResultsMsg.style.display = 'block';
      if(loadMoreContainer) loadMoreContainer.style.display = 'none';
    } else {
      if(noResultsMsg) noResultsMsg.style.display = 'none';
      if (matchedCards.length > visibleLimit) {
        if(loadMoreContainer) loadMoreContainer.style.display = 'block';
      } else {
        if(loadMoreContainer) loadMoreContainer.style.display = 'none';
      }
    }
  }

  // 5. EVENTOS DE BÚSQUEDA Y BOTÓN "VER MÁS"
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
// 1. EL DICCIONARIO DE TRADUCCIONES
const traducciones = {
  "EU": {
    "nav_back": "Itzuli hasierara",
    "cat_title": "Inbentario Osoa",
    "cat_sub": "Hemen aurkituko dituzu Bermeoko Udalaren biltegian dauden objektu guztiak. Zure bat ikusten baduzu, sakatu erreklamatzeko botoia.",
    "search_place": "Bilatu objektuaren izena...",
    "btn_claim": "Mezu bat bidali"
  },
  "ES": {
    "nav_back": "Volver al inicio",
    "cat_title": "Inventario Completo",
    "cat_sub": "Aquí encontrarás todos los objetos almacenados por el Ayuntamiento de Bermeo. Si reconoces el tuyo, pulsa en el botón para reclamarlo.",
    "search_place": "Buscar nombre del objeto...",
    "btn_claim": "Enviar mensaje"
  }
};

// 2. LA FUNCIÓN QUE CAMBIA EL IDIOMA
function setLang(idioma) {
  // A) Guardamos el idioma en el navegador para que no se borre al cambiar de página
  localStorage.setItem('appLang', idioma);

  // B) Cambiamos el color de los botones (Fondo azul al seleccionado)
  document.querySelectorAll('.lang-btn').forEach(btn => {
    const isActive = btn.textContent === idioma;
    btn.classList.toggle('active', isActive);
    btn.setAttribute('aria-pressed', isActive);
  });

  // C) Traducimos los textos
  document.querySelectorAll('[data-i18n]').forEach(elemento => {
    const clave = elemento.getAttribute('data-i18n');
    
    if (traducciones[idioma] && traducciones[idioma][clave]) {
      // Si es un input de texto, le cambiamos el 'placeholder'
      if (elemento.tagName === 'INPUT' || elemento.tagName === 'TEXTAREA') {
        elemento.placeholder = traducciones[idioma][clave];
      } else {
        // Si es texto normal, cambiamos el contenido
        elemento.textContent = traducciones[idioma][clave];
      }
    }
  });
}

// 3. AL CARGAR LA PÁGINA, RECORDAR EL IDIOMA
document.addEventListener('DOMContentLoaded', () => {
  // Miramos si el usuario ya había elegido un idioma antes. Si no, ponemos 'EU' por defecto.
  const idiomaGuardado = localStorage.getItem('appLang') || 'EU';
  setLang(idiomaGuardado);
});