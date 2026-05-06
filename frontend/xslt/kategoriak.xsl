<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html lang="eu">
        <head>
            <meta charset="UTF-8"/>
            <title>Kategoriak - XSLT</title>
            <style>
                body { 
                    font-family: 'Inter', sans-serif; 
                    background-color: #F6F7FA; 
                    padding: 40px; 
                    text-align: center;
                }
                h1 { color: #0A1E5E; margin-bottom: 40px; font-family: 'Montserrat', sans-serif;}
                .grid {
                    display: flex; gap: 20px; justify-content: center; flex-wrap: wrap;
                }
                .card { 
                    background: white; border: 1px solid #EDEEF3; 
                    padding: 20px 30px; border-radius: 12px; 
                    box-shadow: 0 4px 20px rgba(59,91,219,.08);
                    min-width: 150px;
                }
                h2 { color: #3B5BDB; margin: 0 0 10px 0; font-size: 18px;}
                .count { font-size: 24px; font-weight: bold; color: #12131A;}
                .id-label { font-size: 12px; color: #B8BBCA; text-transform: uppercase; margin-top: 10px; display: block;}
            </style>
        </head>
        <body>
            <h1>Kategorien Zerrenda (XSLT Eraldaketa)</h1>
            <div class="grid">
                <xsl:for-each select="kategoriak/kategoria">
                    <div class="card">
                        <h2><xsl:value-of select="izena"/></h2>
                        <div class="count"><xsl:value-of select="kopurua"/> <span style="font-size:14px; font-weight:normal; color:#6B6F80;">objektu</span></div>
                        <span class="id-label">ID: <xsl:value-of select="@id"/></span>
                    </div>
                </xsl:for-each>
            </div>
        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>