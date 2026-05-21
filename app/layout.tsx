import type { Metadata } from 'next'
import { DM_Sans, JetBrains_Mono, Bricolage_Grotesque } from 'next/font/google'
import { Analytics } from '@vercel/analytics/next'
import './globals.css'

const dmSans = DM_Sans({ 
  subsets: ["latin"],
  variable: '--font-dm-sans',
  weight: ['400', '500', '600', '700']
});
const jetbrainsMono = JetBrains_Mono({ 
  subsets: ["latin"],
  variable: '--font-jetbrains-mono',
  weight: ['400', '500', '700']
});
const bricolageGrotesque = Bricolage_Grotesque({ 
  subsets: ["latin"],
  variable: '--font-bricolage',
  weight: ['400', '500', '600', '700', '800']
});

export const metadata: Metadata = {
  title: 'ERRONKA · Triptikoa',
  description: 'Bermeoko Udalaren galdu eta aurkituen kudeaketa-sistema integratua',
  generator: 'v0.app',
  icons: {
    icon: [
      {
        url: '/icon-light-32x32.png',
        media: '(prefers-color-scheme: light)',
      },
      {
        url: '/icon-dark-32x32.png',
        media: '(prefers-color-scheme: dark)',
      },
      {
        url: '/icon.svg',
        type: 'image/svg+xml',
      },
    ],
    apple: '/apple-icon.png',
  },
}

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode
}>) {
  return (
    <html lang="eu" className={`${dmSans.variable} ${jetbrainsMono.variable} ${bricolageGrotesque.variable}`}>
      <body className="font-sans antialiased bg-background">
        {children}
        {process.env.NODE_ENV === 'production' && <Analytics />}
      </body>
    </html>
  )
}
