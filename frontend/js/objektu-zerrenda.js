document.addEventListener("DOMContentLoaded", () => {
  const catalogGrid = document.getElementById('catalogGrid');
  const searchInput = document.getElementById('searchInput');
  const categorySelect = document.getElementById('categorySelect');
  const noResultsMsg = document.getElementById('noResults');
  const loadingMsg = document.getElementById('loadingMsg');
  
  // Nuevas variables para el botón "Ver más"
  const loadMoreContainer = document.getElementById('loadMoreContainer');
  const btnLoadMore = document.getElementById('btnLoadMore');
  
  let cards = []; // Todas las tarjetas generadas
  let visibleLimit = 6; // Límite inicial de tarjetas a mostrar

  // 1. CARGAR DATOS DESDE EL XML
  fetch('../datuak/artikuluak.xml')
    .then(response => {
      if (!response.ok) throw new Error("Ezin izan da XML fitxategia kargatu");
      return response.text();
    })
    .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
    .then(xmlDoc => {
      const artikuluak = xmlDoc.querySelectorAll("artikulua");
      catalogGrid.innerHTML = ''; // Limpiar "Kargatzen..."

      // CREAR LAS TARJETAS DINÁMICAMENTE (Ocultas por defecto)
      artikuluak.forEach(art => {
        const izenburua = art.querySelector("izenburua") ? art.querySelector("izenburua").textContent : 'Izen gabea';
        const kategoria = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : 'Bestelakoak';
        
        const defaultImg = `
          <div class="img-placeholder">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="M21 15l-5-5L5 21"/></svg>
            <span style="font-size: 11px;">Argazkirik gabe</span>
          </div>`;

        const card = document.createElement('div');
        card.className = 'item-card';
        card.setAttribute('data-category', kategoria);
        card.style.display = 'none'; // Las creamos ocultas
        
        card.innerHTML = `
          <div class="item-img">${defaultImg}</div>
          <div class="item-info">
            <span class="item-cat-badge">${kategoria.toUpperCase()}</span>
            <h3 class="item-name">${izenburua}</h3>
            <a href="mailto:galduaurkituak@bermeo.eus?subject=Reclamacion:%20${encodeURIComponent(izenburua)}" class="btn-claim">Mezu bat bidali</a>
          </div>
        `;
        catalogGrid.appendChild(card);
      });

      // Convertimos los elementos HTML en un array real para poder filtrarlos
      cards = Array.from(document.querySelectorAll('.item-card'));
      
      // Llamamos a la función para que muestre las 6 primeras
      filterItems(); 
    })
    .catch(error => {
      console.error("Errorea:", error);
      if (loadingMsg) loadingMsg.textContent = "Errorea datuak kargatzean. / Error al cargar datos.";
    });

  // 2. LÓGICA DE FILTRADO Y LÍMITES
  function filterItems() {
    const searchTerm = searchInput.value.toLowerCase();
    const selectedCategory = categorySelect.value;

    // A) Encontrar qué tarjetas coinciden con los filtros actuales
    let matchedCards = cards.filter(card => {
      const itemName = card.querySelector('.item-name').textContent.toLowerCase();
      const itemCategory = card.getAttribute('data-category');
      
      const matchesSearch = itemName.includes(searchTerm);
      const matchesCategory = selectedCategory === 'all' || itemCategory === selectedCategory;
      
      return matchesSearch && matchesCategory;
    });

    // B) Ocultar todas las tarjetas primero por seguridad
    cards.forEach(card => card.style.display = 'none');

    // C) Mostrar solo las coincidentes hasta llegar al límite (visibleLimit)
    for (let i = 0; i < matchedCards.length; i++) {
      if (i < visibleLimit) {
        matchedCards[i].style.display = 'flex';
      }
    }

    // D) Controlar qué pasa con los mensajes y el botón "Ver más"
    if (matchedCards.length === 0 && cards.length > 0) {
      // No hay resultados
      noResultsMsg.style.display = 'block';
      loadMoreContainer.style.display = 'none';
    } else {
      noResultsMsg.style.display = 'none';
      
      // Si hay más resultados guardados que los que se están mostrando ahora mismo
      if (matchedCards.length > visibleLimit) {
        loadMoreContainer.style.display = 'block'; // Mostramos el botón
      } else {
        loadMoreContainer.style.display = 'none'; // Escondemos el botón porque ya se ven todos
      }
    }
  }

  // 3. RESETEAR EL LÍMITE AL BUSCAR O FILTRAR
  function onFilterChange() {
    visibleLimit = 6; // Si el usuario busca algo, volvemos a empezar contando 6
    filterItems();
  }

  searchInput.addEventListener('input', onFilterChange);
  categorySelect.addEventListener('change', onFilterChange);

  // 4. ACCIÓN DEL BOTÓN "VER MÁS"
  btnLoadMore.addEventListener('click', () => {
    visibleLimit += 1000; // Un número gigante para mostrar el resto de golpe
    filterItems(); // Refrescamos la vista
  });
});