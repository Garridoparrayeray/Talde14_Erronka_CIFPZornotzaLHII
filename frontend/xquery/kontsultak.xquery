(: RA 6: XQuery Kontsultak (3 bilaketa gutxienez) :)

<emaitzak>
{
  (: 1. Artikulu guztiak izenaren arabera ordenatuta :)
  for $a in doc("../datuak/artikuluak.xml")/artikuluak/artikulua
  order by $a/izena
  return $a/izena,

  (: 2. Kategoria bakoitzeko artikulu kopurua :)
  for $c in doc("../datuak/kategoriak.xml")/kategoriak/kategoria
  let $nombre := $c/izena
  return <resumen kategoria="{$nombre/text()}"> { $c/kopurua/text() } </resumen>,

  (: 3. 'Gakoak' kategoria duten artikuluen IDak soilik :)
  <ids_gakoak>
  {
    for $a in doc("../datuak/artikuluak.xml")/artikuluak/artikulua
    where $a/kategoria = 'Gakoak'
    return $a/id
  }
  </ids_gakoak>
}
</emaitzak>