"use client"

import Image from "next/image"
import { Download } from "lucide-react"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-[#15141a] flex items-center justify-center p-6" style={{
      background: `
        radial-gradient(800px 500px at 30% 10%, #1f2240 0%, transparent 60%),
        radial-gradient(600px 400px at 70% 90%, #1a1a2e 0%, transparent 50%),
        #15141a
      `
    }}>
      <div className="w-full max-w-md text-center">
        {/* Logo */}
        <div className="mb-8 flex justify-center">
          <div className="bg-white rounded-xl p-6 shadow-lg">
            <Image
              src="/images/logo-bermeoko-udala.png"
              alt="Bermeoko Udala"
              width={240}
              height={80}
              className="w-auto h-auto max-w-[240px]"
              priority
            />
          </div>
        </div>

        {/* Title */}
        <h1 className="font-heading font-bold text-3xl md:text-4xl tracking-tight text-white mb-2">
          ERRONKA<span className="text-[#E2542C]">·</span>Triptikoa
        </h1>
        <p className="text-[#9590a8] text-base mb-8">
          Galdu eta aurkituen kudeaketa-sistema
        </p>

        {/* Language label */}
        <p className="text-[12px] tracking-widest uppercase text-[#6b6780] mb-4">
          Deskargatu triptikoa / Descargar tríptico
        </p>

        {/* Download buttons */}
        <div className="flex flex-col gap-3">
          <a
            href="/triptikoa"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center justify-center gap-3 px-6 py-4 rounded-xl bg-[#2A45D4] text-white font-semibold text-lg shadow-lg hover:bg-[#3652e6] transition-colors"
          >
            <span className="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center text-sm font-bold">
              EU
            </span>
            Euskaraz
            <Download className="w-5 h-5 ml-auto opacity-60" />
          </a>

          <a
            href="/triptico"
            target="_blank"
            rel="noopener noreferrer"
            className="flex items-center justify-center gap-3 px-6 py-4 rounded-xl bg-white/10 text-white font-semibold text-lg border border-white/20 hover:bg-white/15 transition-colors"
          >
            <span className="w-8 h-8 rounded-full bg-white/20 flex items-center justify-center text-sm font-bold">
              ES
            </span>
            Castellano
            <Download className="w-5 h-5 ml-auto opacity-60" />
          </a>
        </div>

        {/* Instructions */}
        <p className="mt-6 text-[13px] text-[#6b6780] leading-relaxed">
          Ireki eta inprimatu PDF gisa gordetzeko.<br/>
          Abre e imprime para guardar como PDF.
        </p>

        {/* Footer */}
        <div className="mt-10 pt-6 border-t border-white/10 flex items-center justify-between text-[11px] tracking-widest uppercase text-[#6b6780]">
          <span>v1.0 · 2026</span>
          <span>CIFP Zornotza LHII</span>
        </div>
      </div>
    </div>
  )
}
