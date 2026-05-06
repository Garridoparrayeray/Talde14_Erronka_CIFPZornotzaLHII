<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <html lang="eu">
        <head>
            <meta charset="UTF-8"/>
            <title>Galdutako Objektuak - XSLT</title>
            <style>
                body { 
                    font-family: 'Inter', sans-serif; 
                    background-color: #F6F7FA; 
                    color: #12131A; 
                    padding: 40px; 
                }
                h1 { color: #0A1E5E; text-align: center; margin-bottom: 30px; font-family: 'Montserrat', sans-serif;}
                table { 
                    width: 100%; max-width: 900px; margin: 0 auto; 
                    border-collapse: collapse; background: #fff; 
                    border-radius: 10px; overflow: hidden;
                    box-shadow: 0 12px 48px rgba(59,91,219,.18); 
                }
                th, td { padding: 16px 20px; border-bottom: 1px solid #EDEEF3; text-align: left; }
                th { background-color: #3B5BDB; color: #fff; text-transform: uppercase; font-size: 13px; letter-spacing: 0.05em;}
                tr:hover { background-color: #F2F5FD; }
                tr:last-child td { border-bottom: none; }
                .kategoria { font-weight: 600; color: #3B5BDB; text-transform: uppercase; font-size: 11px; background: #E8EDFB; padding: 4px 8px; border-radius: 4px;}
            </style>
        </head>
        <body>
            <h1>Inbentarioa (XSLT Eraldaketa)</h1>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Izena</th>
                        <th>Kategoria</th>
                        <th>Deskribapena</th>
                        <th>Data</th>
                    </tr>
                </thead>
                <tbody>
                    <xsl:for-each select="artikuluak/artikulua">
                        <tr>
                            <td><xsl:value-of select="id"/></td>
                            <td style="font-weight: 600;"><xsl:value-of select="izena"/></td>
                            <td><span class="kategoria"><xsl:value-of select="kategoria"/></span></td>
                            <td style="font-size: 14px; color: #6B6F80;"><xsl:value-of select="deskribapena"/></td>
                            <td><xsl:value-of select="sarreraData"/></td>
                        </tr>
                    </xsl:for-each>
                </tbody>
            </table>
        </body>
        </html>
    </xsl:template>
</xsl:stylesheet>