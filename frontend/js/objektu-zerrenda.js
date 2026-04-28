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