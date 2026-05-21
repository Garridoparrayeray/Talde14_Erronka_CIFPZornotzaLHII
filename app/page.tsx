import Link from "next/link"
import Image from "next/image"

export default function HomePage() {
  return (
    <div className="min-h-screen bg-[#15141a] flex items-center justify-center py-16 px-6" style={{
      background: `
        radial-gradient(1200px 800px at 20% 0%, #2a2a36 0%, transparent 60%),
        radial-gradient(1000px 700px at 85% 100%, #221f2a 0%, transparent 55%),
        #15141a
      `
    }}>
      <div className="max-w-[700px] w-full text-center">
        {/* Logo */}
        <div className="mb-10 flex justify-center">
          <div className="bg-white rounded-2xl p-8 shadow-[0_30px_50px_-25px_rgba(0,0,0,.45)]">
            <Image
              src="/images/logo-bermeoko-udala.png"
              alt="Bermeoko Udala"
              width={320}
              height={107}
              className="w-full h-auto max-w-[320px] mx-auto block"
            />
          </div>
        </div>

        {/* Title */}
        <h1 className="font-heading font-bold text-[48px] md:text-[64px] tracking-tight text-white leading-[1.05] mb-4">
          ERRONKA<span className="text-[#E2542C]">·</span>Triptikoa
        </h1>
        <p className="text-[#9590a8] text-[20px] md:text-[24px] max-w-[520px] mx-auto leading-[1.5] mb-12">
          Sistema integrado de gestión de objetos perdidos y encontrados
        </p>

        {/* Language selection */}
        <div className="text-[14px] tracking-[.2em] uppercase text-[#8a8576] mb-6">
          Aukeratu hizkuntza · Elige idioma
        </div>

        <div className="flex flex-col sm:flex-row gap-4 justify-center">
          <Link 
            href="/triptikoa"
            className="group flex items-center justify-center gap-4 px-8 py-5 rounded-[14px] bg-[#2A45D4] text-white font-heading font-bold text-[22px] tracking-[-0.01em] shadow-[0_20px_40px_-15px_rgba(42,69,212,.5)] hover:bg-[#3652e6] transition-colors"
          >
            <span className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center text-[16px] font-bold">
              EU
            </span>
            Euskaraz
          </Link>
          
          <Link 
            href="/triptico"
            className="group flex items-center justify-center gap-4 px-8 py-5 rounded-[14px] bg-white/[0.08] text-white font-heading font-bold text-[22px] tracking-[-0.01em] border border-white/[0.15] hover:bg-white/[0.12] transition-colors"
          >
            <span className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center text-[16px] font-bold">
              ES
            </span>
            En Castellano
          </Link>
        </div>

        {/* Footer info */}
        <div className="mt-16 pt-8 border-t border-white/[0.1] flex flex-col sm:flex-row items-center justify-between gap-4 text-[13px] tracking-[.12em] uppercase text-[#8a8576]">
          <span>v 1.0 · 2026</span>
          <span>1. DAW · CIFP Zornotza LHII</span>
        </div>
      </div>

    </div>
  )
}
