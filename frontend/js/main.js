// ==========================================================================
// 1. ITZULPENEN HIZTEGIA (HASIERAKO ORRIA)
// ==========================================================================
const dictIndex = {
  "EU": {
    "nav_home": "Hasiera", "nav_claim": "Erreklamazioa", "nav_process": "Prozesua", "nav_cats": "Kategoriak", "nav_help": "Laguntza", "nav_btn_claim": "Erreklamatu",
    "hero_title": "Galdutakoa berriro aurkitu", "hero_sub": "Bermeoko Udalaren galdu eta aurkituen zerbitzu digitala. Galdu duzuna bilatzen laguntzeko eta aurkitutakoa erreklamatzeko plataforma bakarra.", "hero_btn": "Erreklamazioa hasi",
    "mc_brand": "Marka", "mc_color": "Kolorea",
    "form_tag": "Erreklamazioa", "form_title": "Galdutako objektua erreklamatu", "form_sub": "Bete inprimakia galdutako objektuaren xehetasunekin.", "form_sec1": "1. Eskatzailearen Datuak", "form_nan": "NAN / NIE", "form_place_nan": "12345678A", "form_name": "Izena", "form_place_name": "Sartu zure izena", "form_surname": "Abizenak", "form_place_surname": "Sartu zure abizenak", "form_phone": "Telefonoa", "form_place_phone": "adib. 600 000 000", "form_email": "Posta elektronikoa", "form_place_email": "adib. izena@email.com",
    "form_sec2": "2. Galdutako Objektuaren Deskribapena", "form_desc": "Deskribapena (kolorea, marka, materiala...)", "form_place_desc": "Eman xehetasun guztiak (marka, kolorea, edukia...)", "form_photo": "Argazkia erantsi (Aukerakoa)", "form_remove_btn": "Artxiboa ezabatu",
    "form_sec3": "3. Noiz eta Non", "form_date": "Galtze-data", "form_loc": "Kokalekua", "form_opt_choose": "Aukeratu bat", "form_opt_port": "Bermeoko Portua", "form_opt_gazt": "San Juan de Gaztelugatxe", "form_opt_street": "Bide publikoa", "form_opt_trans": "Garraio publikoa (Bizkaibus, Euskotren)", "form_opt_hall": "Udaletxea", "form_opt_other": "Bestelakoak", "form_notes": "Oharrak", "form_place_notes": "Informazio gehigarria...", "form_privacy_1": "Datu pertsonalen babesari buruzko informazioa ", "form_privacy_link": "irakurri eta onartzen dut", "form_submit": "Bidali erreklamazioa",
    "proc_tag": "Nola funtzionatzen du", "proc_title": "Hiru urrats, itzulera bat", "proc_sub": "Prozesu erraza, gardena eta bizkorra. Bermeoko Udaleko langileek lagundu egingo dizute urrats guztietan.", "step1_title": "Bilatu inbentarioan", "step1_desc": "Bilaketa aurreratua erabili galdu duzuna aurkitzeko. Kategoria, kolorea, data eta kokalaguntzak iragazki gisa erabil ditzakezu.", "step2_title": "Erreklamazioa bidali", "step2_desc": "Objektua aurkitu baduzu, erreklamazioformen bidez zure datuak bidali. Txartel nazionala edo NAN zenbakia behar duzu egiaztatzeko.", "step3_title": "Jaso udaletxean", "step3_desc": "Zure erreklamazioa onartu ondoren, hitzordua eskatu Bermeoko Udaleko BerHAZ bulegoan (Ertzilla kalea 6-8) objektua pertsonalki jasotzeko. Hau da hain sinplea!",
    "cats_tag": "Katalogoa", "cats_title": "Kategoriak", "cats_see_all": "Denak ikusi", "cats_loading": "Kategoriak kargatzen...",
    "foot_desc": "Bermeoko Udaleko galdu eta aurkituen zerbitzu ofiziala. Herritarrei zerbitzuan, gardentasunez eta eraginkortasunez.", "foot_col1": "Zerbitzuak", "foot_link_inv": "Inbentarioa bilatu", "foot_link_claim": "Erreklamazioa hasi", "foot_link_my": "Nire erreklamazioak", "foot_link_not": "Jakinarazpenak", "foot_link_faq": "Galdera ohikoak", "foot_col2": "Udala", "foot_link_town": "Bermeoko Udala", "foot_link_serv": "Zerbitzu guztiak", "foot_link_board": "Iragarki-taula", "foot_link_press": "Prentsa-kabineta", "foot_link_contact": "Kontaktua", "foot_col3": "Legala", "foot_link_priv": "Pribatutasun-politika", "foot_link_terms": "Erabileraren baldintzak", "foot_link_cook": "Cookie politika", "foot_link_acc": "Irisgarritasuna", "foot_legal_text": "v1.0 · © 2026 Bermeoko Udala · Eskubide guztiak erreserbatuta · Eder Martin Mosquerak eta Yeray Garrido Parrak maitasun eta kafeina handiz egina.",
    "modal_privacy_body": "Bermeoko Udalak datu pertsonalen tratamendua bermatzen du indarrean dagoen legeriaren arabera (RGPD).", "modal_close": "Itxi", "form_success": "Erreklamazioa ondo sortu da! Fitxategia deskargatzen..."
  },
  "ES": {
    "nav_home": "Inicio", "nav_claim": "Reclamación", "nav_process": "Proceso", "nav_cats": "Categorías", "nav_help": "Ayuda", "nav_btn_claim": "Reclamar",
    "hero_title": "Encuentra de nuevo lo perdido", "hero_sub": "Servicio digital de objetos perdidos del Ayuntamiento de Bermeo. La plataforma única para ayudarte a buscar lo que has perdido y reclamar lo encontrado.", "hero_btn": "Iniciar reclamación",
    "mc_brand": "Marca", "mc_color": "Color",
    "form_tag": "Reclamación", "form_title": "Reclamar objeto perdido", "form_sub": "Rellena el formulario con los detalles del objeto perdido.", "form_sec1": "1. Datos del Solicitante", "form_nan": "DNI / NIE", "form_place_nan": "12345678A", "form_name": "Nombre", "form_place_name": "Introduce tu nombre", "form_surname": "Apellidos", "form_place_surname": "Introduce tus apellidos", "form_phone": "Teléfono", "form_place_phone": "ej. 600 000 000", "form_email": "Correo electrónico", "form_place_email": "ej. nombre@email.com",
    "form_sec2": "2. Descripción del Objeto Perdido", "form_desc": "Descripción (color, marca, material...)", "form_place_desc": "Da todos los detalles (marca, color, contenido...)", "form_photo": "Adjuntar foto (Opcional)", "form_remove_btn": "Borrar archivo",
    "form_sec3": "3. Cuándo y Dónde", "form_date": "Fecha de pérdida", "form_loc": "Ubicación", "form_opt_choose": "Elige una", "form_opt_port": "Puerto de Bermeo", "form_opt_gazt": "San Juan de Gaztelugatxe", "form_opt_street": "Vía pública", "form_opt_trans": "Transporte público (Bizkaibus, Euskotren)", "form_opt_hall": "Ayuntamiento", "form_opt_other": "Otros", "form_notes": "Notas", "form_place_notes": "Información adicional...", "form_privacy_1": "He leído y acepto la información sobre protección de datos personales ", "form_privacy_link": "haciendo clic aquí", "form_submit": "Enviar reclamación",
    "proc_tag": "Cómo funciona", "proc_title": "Tres pasos, una devolución", "proc_sub": "Proceso fácil, transparente y rápido. Los trabajadores del Ayuntamiento de Bermeo te ayudarán en todos los pasos.", "step1_title": "Busca en el inventario", "step1_desc": "Usa la búsqueda avanzada para encontrar lo que has perdido. Puedes usar categoría, color y fecha como filtros.", "step2_title": "Envía una reclamación", "step2_desc": "Si has encontrado el objeto, envía tus datos mediante el formulario. Necesitarás tu DNI para verificarlo.", "step3_title": "Recoge en el ayuntamiento", "step3_desc": "Una vez aceptada tu reclamación, pide cita para recoger el objeto personalmente en la oficina BerHAZ del Ayuntamiento de Bermeo (C/ Ertzilla, 6-8).",
    "cats_tag": "Catálogo", "cats_title": "Categorías", "cats_see_all": "Ver todas", "cats_loading": "Cargando categorías...",
    "foot_desc": "Servicio oficial de objetos perdidos del Ayuntamiento de Bermeo. Al servicio de los ciudadanos con transparencia y eficacia.", "foot_col1": "Servicios", "foot_link_inv": "Buscar en inventario", "foot_link_claim": "Iniciar reclamación", "foot_link_my": "Mis reclamaciones", "foot_link_not": "Notificaciones", "foot_link_faq": "Preguntas frecuentes", "foot_col2": "Ayuntamiento", "foot_link_town": "Ayuntamiento de Bermeo", "foot_link_serv": "Todos los servicios", "foot_link_board": "Tablón de anuncios", "foot_link_press": "Gabinete de prensa", "foot_link_contact": "Contacto", "foot_col3": "Legal", "foot_link_priv": "Política de privacidad", "foot_link_terms": "Condiciones de uso", "foot_link_cook": "Política de cookies", "foot_link_acc": "Accesibilidad", "foot_legal_text": "v1.0 · © 2026 Ayuntamiento de Bermeo · Todos los derechos reservados · Hecho con mucho amor y cafeína por Eder Martin Mosquera y Yeray Garrido Parra.",
    "modal_privacy_body": "El Ayuntamiento de Bermeo garantiza el tratamiento de datos personales de acuerdo a la legislación vigente (RGPD).", "modal_close": "Cerrar", "form_success": "¡Reclamación creada con éxito! Descargando archivo..."
  }
};

// Hizkuntza aldatzeko funtzioa
function setLang(idioma) {
  localStorage.setItem('appLang', idioma);
  
  // Botoi bisualak eguneratu
  document.querySelectorAll('.lang-btn').forEach(btn => {
    const isActive = btn.textContent === idioma;
    btn.classList.toggle('active', isActive);
    btn.setAttribute('aria-pressed', isActive);
  });
  
  // Testuak itzuli
  document.querySelectorAll('[data-i18n]').forEach(el => {
    const clave = el.getAttribute('data-i18n');
    if (dictIndex[idioma] && dictIndex[idioma][clave]) {
      if (el.tagName === 'INPUT' || el.tagName === 'TEXTAREA') {
        el.placeholder = dictIndex[idioma][clave];
      } else {
        el.textContent = dictIndex[idioma][clave];
      }
    }
  });

  // "Objektu/objetos" hitza dinamikoki itzuli kategorietan kargatuta badaude
  document.querySelectorAll('.cat-count').forEach(count => {
    const num = count.textContent.replace(/[^0-9]/g, ''); // Zenbakia bakarrik lortu
    if(num) {
      count.textContent = idioma === 'ES' ? `${num} objetos` : `${num} objektu`;
    }
  });
}


// ==========================================================================
// 2. ORRIAREN LOGIKA (Scroll, XML, Inprimakia...)
// ==========================================
document.addEventListener('DOMContentLoaded', () => {
  // 1. Hizkuntza hasieratu
  setLang(localStorage.getItem('appLang') || 'EU');

  // 2. Nabigazio-barraren logika (Scroll)
  const navbar = document.getElementById("navbar");
  const navLinks = document.querySelectorAll(".nav-links a");
  const sectionsToTrack = [
    { id: "index.html", element: document.querySelector(".hero") },
    { id: "#claim", element: document.getElementById("claim") },
    { id: "#process", element: document.getElementById("process") },
    { id: "#categories", element: document.getElementById("categories") },
    { id: "#contact", element: document.getElementById("contact") }
  ];

  if(navbar) {
      window.addEventListener("scroll", () => {
        navbar.classList.toggle("scrolled", window.scrollY > 20);
        
        let currentSectionId = "index.html";
        sectionsToTrack.forEach(section => {
          if (section.element) {
            const offsetTop = section.element.offsetTop;
            if (window.scrollY >= offsetTop - 200) {
              currentSectionId = section.id;
            }
          }
        });

        if (window.innerHeight + Math.round(window.scrollY) >= document.body.offsetHeight - 50) {
          currentSectionId = "#contact";
        }

        navLinks.forEach(link => {
          link.classList.remove("active");
          if (link.getAttribute("href") === currentSectionId) {
            link.classList.add("active");
          }
        });
      });
  }

  // 3. Mugikorreko menua
  const menuToggle = document.getElementById('menuToggle');
  const mobileNav = document.getElementById('mobileNav');
  if(menuToggle && mobileNav) {
      menuToggle.addEventListener('click', () => {
          const isExpanded = menuToggle.getAttribute('aria-expanded') === 'true';
          menuToggle.setAttribute('aria-expanded', !isExpanded);
          mobileNav.style.display = isExpanded ? 'none' : 'flex';
      });
  }

  // 4. Scroll leuna menuko esteketarako
  document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener("click", function (e) {
      const targetId = this.getAttribute("href");
      if ("#" === targetId) {
        e.preventDefault();
        window.scrollTo({ top: 0, behavior: "smooth" });
        return;
      }
      const targetElement = document.querySelector(targetId);
      if (targetElement) {
        e.preventDefault();
        targetElement.scrollIntoView({ behavior: "smooth" });
      }
    });
  });
  
  // 5. Inprimakiko fitxategi-botoia
  const fileInput = document.getElementById("claim-file");
  const removeFileBtn = document.getElementById("remove-file-btn");
  if (fileInput && removeFileBtn) {
      fileInput.addEventListener('change', () => {
          if(fileInput.files.length > 0) {
              removeFileBtn.classList.remove('d-none');
              removeFileBtn.classList.add('d-flex');
          } else {
              removeFileBtn.classList.add('d-none');
              removeFileBtn.classList.remove('d-flex');
          }
      });
      removeFileBtn.addEventListener('click', () => {
          fileInput.value = '';
          removeFileBtn.classList.add('d-none');
          removeFileBtn.classList.remove('d-flex');
      });
  }

  // 6. Kategoriak dinamikoki kargatu XML-tik
  const categoryGrid = document.getElementById('categoryGrid');
  if (categoryGrid) {
    const katEstiloak = {
      "jantziak": { bg: "#EEF2FF", color: "#3B5BDB", svg: '<rect x="2" y="7" width="20" height="14" rx="2"/><path d="M16 7V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v2"/>' },
      "gakoak": { bg: "#FFF3D4", color: "#E09A10", svg: '<path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"/><circle cx="12" cy="10" r="3"/>' },
      "elektronika": { bg: "#E4F9ED", color: "#2BB673", svg: '<rect x="5" y="2" width="14" height="20" rx="2" ry="2"/><line x1="12" y1="18" x2="12.01" y2="18"/>' },
      "dokumentuak": { bg: "#FDE8E8", color: "#E84040", svg: '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14,2 14,8 20,8"/>' },
      "eguzkitakoak": { bg: "#F0EDFF", color: "#7B5FDC", svg: '<circle cx="12" cy="12" r="1"/><circle cx="19" cy="12" r="1"/><circle cx="5" cy="12" r="1"/>' },
      "bestelakoak": { bg: "#E6F4F1", color: "#0D9488", svg: '<line x1="12" y1="2" x2="12" y2="6"/><path d="M6.3 6.3l-2.8-2.8M17.7 6.3l2.8-2.8M6 12H2M22 12h-4M6.3 17.7l-2.8 2.8M17.7 17.7l2.8 2.8M12 18v4"/><circle cx="12" cy="12" r="4"/>' }
    };

    fetch('/datuak/artikuluak.xml')
      .then(response => response.text())
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const artikuluak = xmlDoc.querySelectorAll("artikulua");
        categoryGrid.innerHTML = ''; 

        // Kategoriak dinamikoki elkartu eta zenbatu
        const counts = {};
        artikuluak.forEach(art => {
          const katName = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : 'Bestelakoak';
          counts[katName] = (counts[katName] || 0) + 1;
        });

        Object.entries(counts).forEach(([izena, kopurua]) => {
          // Izen xehea erabili estiloa bilatzeko (adib: "Jantziak" -> "jantziak")
          const styleKey = izena.toLowerCase();
          
          const estilo = katEstiloak[styleKey] || { bg: "#F2F5FD", color: "#6B6F80", svg: '<circle cx="12" cy="12" r="10"/><path d="M12 8v4l3 3"/>' };

          const card = document.createElement('a');
          card.href = 'html/objektu-zerrenda.html';
          card.className = 'cat-card';
          
          const currentLang = localStorage.getItem('appLang') || 'EU';
          const textObj = currentLang === 'ES' ? 'objetos' : 'objektu';

          card.innerHTML = `
            <div class="cat-icon" style="background:${estilo.bg};">
              <svg viewBox="0 0 24 24" style="stroke:${estilo.color}; fill:none; stroke-width:1.8; stroke-linecap:round; stroke-linejoin:round;"><g>${estilo.svg}</g></svg>
            </div>
            <div class="cat-name">${izena}</div>
            <div class="cat-count">${kopurua} ${textObj}</div>
          `;
          categoryGrid.appendChild(card);
        });
      })
      .catch(error => {
        console.error("Errorea:", error);
        categoryGrid.innerHTML = '<p class="text-danger">Errorea datuak kargatzean.</p>';
      });
  }

  // 7. Galdutako objektuen karrusela hasierako txartelean (Hero)
  const mockupCard = document.querySelector('.mockup-card');
  if (mockupCard) {
    mockupCard.addEventListener('click', () => {
      window.location.href = 'html/objektu-zerrenda.html';
    });

    // Hasierako datu asmatuak kendu eta txartela ezkutatu kargatu arte
    mockupCard.innerHTML = '';
    mockupCard.classList.add('mc-hidden');

    fetch('/datuak/artikuluak.xml')
      .then(response => response.text())
      .then(str => new window.DOMParser().parseFromString(str, "text/xml"))
      .then(xmlDoc => {
        const artikuluak = Array.from(xmlDoc.querySelectorAll("artikulua"));
        if (artikuluak.length > 0) {
          let currentIndex = 0;
          
          function updateMockup() {
            const art = artikuluak[currentIndex];
            const izena = art.querySelector("izena") ? art.querySelector("izena").textContent : 'Izen gabea';
            const kategoria = art.querySelector("kategoria") ? art.querySelector("kategoria").textContent : '';
            const deskribapena = art.querySelector("deskribapena") ? art.querySelector("deskribapena").textContent : '-';
            const data = art.querySelector("sarreraData") ? art.querySelector("sarreraData").textContent : '-';

            // Uneko hizkuntza mantendu etiketentzat
            const currentLang = localStorage.getItem('appLang') || 'EU';
            const labelDesc = currentLang === 'ES' ? 'Descripción' : 'Deskribapena';
            const labelDate = currentLang === 'ES' ? 'Fecha' : 'Data';

            // Irteera-animazioa
            mockupCard.classList.add('mc-hidden');

            setTimeout(() => {
              // Txartelaren barrualdea berreraiki XML datuekin
              mockupCard.innerHTML = `
                <div class="mc-header">
                  <div class="mc-icon">
                    <svg viewBox="0 0 24 24"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path><polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline><line x1="12" y1="22.08" x2="12" y2="12"></line></svg>
                  </div>
                  <div>
                    <div class="mc-title">${izena}</div>
                    <div class="mc-sub" style="text-transform: capitalize;">${kategoria}</div>
                  </div>
                </div>
                <div class="mc-field">
                  <div class="mc-label">${labelDesc}</div>
                  <div class="mc-value" style="display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; text-overflow: ellipsis;" title="${deskribapena}">${deskribapena}</div>
                </div>
                <div class="mc-divider"></div>
                <div class="mc-row">
                  <div class="mc-field">
                    <div class="mc-label">${labelDate}</div>
                    <div class="mc-value"><span class="mc-tag">${data}</span></div>
                  </div>
                </div>
              `;
              
              // Sarrera-animazioa
              mockupCard.classList.remove('mc-hidden');
              
              currentIndex = (currentIndex + 1) % artikuluak.length;
            }, 400); 
          }

          // XML-a ondo irakurri bada, lehenengo objektua zuzenean erakutsi (itxaron gabe)
          // eta gero 5 segundoro aldatu
          updateMockup(); 
          setInterval(updateMockup, 5000); 
        }
      })
      .catch(error => console.error("Errorea artikuluak kargatzean (carrusel):", error));
  }

  // 8. Erreklamazio inprimakiaren kudeaketa (XML sortzea)

  const claimForm = document.querySelector('form');
  if (claimForm) {
    claimForm.addEventListener('submit', (e) => {
      e.preventDefault(); // Posta-bezeroa irekitzea ekidin
      
      const formData = new FormData(claimForm);
      const data = Object.fromEntries(formData.entries());

      // --- 1. NAN Baliozkotzea (RA 3 - Murriztapenak) ---
      const nanRegex = /^[0-9]{8}[a-zA-Z]$/;
      const nanBalioa = data.nan || ""; // Ziurtatu HTMLan name="nan" duela

      if (nanBalioa && !nanRegex.test(nanBalioa)) {
        const errorMsg = localStorage.getItem('appLang') === 'ES' 
          ? "Formato de NAN incorrecto (8 números y una letra)." 
          : "NAN formatu okerra (8 zenbaki eta letra bat).";
        alert(errorMsg);
        return; // Gelditu prozesua formatua okerra bada
      }

      // --- 2. Telefonoaren baliozkotzea (JS Bigarren Balidazioa) ---
      const telRegex = /^[0-9]{9}$/;
      const telBalioa = data.telefonoa ? data.telefonoa.replace(/\s+/g, '') : "";
      if (telBalioa && !telRegex.test(telBalioa)) {
        const errorMsg = localStorage.getItem('appLang') === 'ES'
          ? "Formato de teléfono incorrecto (debe tener 9 dígitos)."
          : "Telefono formatu okerra (9 digitu izan behar ditu).";
        alert(errorMsg);
        return;
      }

      // --- XML Sortzea eta Deskargatzea (RA 2 betetzeko) ---
      let xmlContent = '<?xml version="1.0" encoding="UTF-8"?>\n';
      // RA2/RA3: DTD eta XSD erreferentziak gehitu
      xmlContent += '<!DOCTYPE erreklamazioa SYSTEM "../dtd/artikuluak.dtd">\n';
      xmlContent += '<erreklamazioa xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../xsd/artikuluak.xsd">\n';

      let hasData = false;
      
      // 1. Datuak 'name' atributuaren bidez jasotzen saiatu
      formData.forEach((value, key) => {
          if (typeof value === 'string') {
              hasData = true;
              let safeKey = key.replace(/[^a-zA-Z0-9_]/g, '');
              if (!/^[a-zA-Z_]/.test(safeKey)) safeKey = 'eremua_' + safeKey;
              let safeValue = value.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
              xmlContent += `    <${safeKey}>${safeValue}</${safeKey}>\n`;
          }
      });
      
      // 2. Inprimakiak 'name' ez badu, 'id' atributua erabili babes gisa
      if (!hasData) {
          const inputs = claimForm.querySelectorAll('input:not([type="submit"]):not([type="file"]):not([type="checkbox"]), textarea, select');
          inputs.forEach((input, index) => {
              let rawKey = input.name || input.id || `eremua_${index + 1}`;
              let safeKey = rawKey.replace(/[^a-zA-Z0-9_]/g, '');
              if (!/^[a-zA-Z_]/.test(safeKey)) safeKey = 'eremua_' + safeKey;
              
              let safeValue = (input.value || '').replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
              xmlContent += `    <${safeKey}>${safeValue}</${safeKey}>\n`;
          });
      }
      
      // Sortze-data automatikoki gehitu
      const gaur = new Date().toISOString().split('T')[0];
      xmlContent += `    <eskaeraData>${gaur}</eskaeraData>\n`;
      xmlContent += '</erreklamazioa>';

      // XML fitxategiaren deskarga behartu
      const blob = new Blob([xmlContent], { type: 'application/xml' });
      const url = URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;

      // Lortu izena eta abizena fitxategi-izenerako (RA 2)
      const izenGarbia = (data.name || data.izena || 'erabiltzailea').toString().trim().replace(/\s+/g, '_');
      const abizenGarbia = (data.surname || data.abizena || '').toString().trim().replace(/\s+/g, '_');
      const fitxategiIzena = abizenGarbia ? `erreklamazioa_${abizenGarbia}_${izenGarbia}.xml` : `erreklamazioa_${izenGarbia}.xml`;

      a.download = fitxategiIzena;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      URL.revokeObjectURL(url);

      // Erakutsi Bootstrap Toast-a
      if (typeof bootstrap !== 'undefined') {
        const toastEl = document.getElementById('successToast');
        if (toastEl) {
          const toast = new bootstrap.Toast(toastEl);
          toast.show();
        }
      }
    });
  }
});
// ==========================================================================
// 9. MODU ILUNA (DARK MODE KUDEAKETA)
// ==========================================================================
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

document.addEventListener('DOMContentLoaded', () => {
  initTheme();
});