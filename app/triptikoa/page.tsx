"use client"

import { useEffect, useRef } from "react"
import Image from "next/image"

export default function TriptikoPage() {
  const zoomWrapRef = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const TARGET = 2200
    function fit() {
      if (!zoomWrapRef.current) return
      const margin = 80
      const avail = Math.max(800, Math.min(window.innerWidth - margin * 2, 1700))
      const zoom = avail / TARGET
      zoomWrapRef.current.style.zoom = String(zoom)
    }
    window.addEventListener("resize", fit)
    fit()
    return () => window.removeEventListener("resize", fit)
  }, [])

  return (
    <div className="min-h-screen bg-[#15141a] py-16 px-0" style={{
      background: `
        radial-gradient(1200px 800px at 20% 0%, #2a2a36 0%, transparent 60%),
        radial-gradient(1000px 700px at 85% 100%, #221f2a 0%, transparent 55%),
        #15141a
      `
    }}>
      {/* Page header */}
      <div className="max-w-[2200px] mx-auto mb-9 px-10 flex items-end justify-between gap-6">
        <div className="font-heading font-bold text-[28px] tracking-tight text-white">
          ERRONKA<span className="text-[#E2542C]">·</span>Triptikoa
        </div>
        <div className="text-[13px] tracking-[.12em] uppercase text-[#8a8576] text-right leading-relaxed">
          <div>Bermeoko Udala · 2026</div>
          <div><span className="text-[#cfc9bd]">11 × 8.5 in · Tri-fold · CMYK ready</span></div>
        </div>
      </div>

      <div ref={zoomWrapRef} style={{ zoom: 0.68 }}>
        <div className="flex flex-col items-center gap-14">
          {/* Outside spread label */}
          <div className="relative w-[2200px] -mb-11 flex items-center gap-3.5 text-[#cfc9bd] text-[12px] tracking-[.24em] uppercase">
            <span className="font-heading font-bold text-white tracking-[.16em]">Kanpoaldea</span>
            <span>· Outside spread · Atzealdea · Solapa · Azala</span>
            <div className="flex-1 h-px bg-white/[0.12]" />
            <span>v1.0</span>
          </div>

          {/* OUTSIDE SPREAD */}
          <section className="spread">
            {/* Inside flap (LEFT) */}
            <PanelFlap />
            {/* Back cover (MIDDLE) */}
            <PanelBack />
            {/* Front cover (RIGHT) */}
            <PanelFront />
          </section>

          {/* Inside spread label */}
          <div className="relative w-[2200px] -mb-11 flex items-center gap-3.5 text-[#cfc9bd] text-[12px] tracking-[.24em] uppercase">
            <span className="font-heading font-bold text-white tracking-[.16em]">Barrualdea</span>
            <span>· Inside spread · Arkitektura · JavaFX · Web</span>
            <div className="flex-1 h-px bg-white/[0.12]" />
            <span>Reads L→R</span>
          </div>

          {/* INSIDE SPREAD */}
          <section className="spread">
            {/* Architecture */}
            <PanelArchitecture />
            {/* JavaFX */}
            <PanelJavaFX />
            {/* Web */}
            <PanelWeb />
          </section>
        </div>
      </div>

      <style jsx global>{`
        .spread {
          width: 2200px;
          height: 1700px;
          background: #F6F4EE;
          box-shadow:
            0 60px 80px -40px rgba(0,0,0,.55),
            0 30px 40px -20px rgba(0,0,0,.4),
            0 2px 0 rgba(255,255,255,.04) inset;
          position: relative;
          display: grid;
          grid-template-columns: 1fr 1fr 1fr;
          overflow: hidden;
          border-radius: 2px;
        }
        .spread::before {
          content: "";
          position: absolute;
          inset: 0;
          pointer-events: none;
          background-image:
            radial-gradient(rgba(0,0,0,.03) 1px, transparent 1px),
            radial-gradient(rgba(255,255,255,.04) 1px, transparent 1px);
          background-size: 3px 3px, 5px 5px;
          background-position: 0 0, 1px 2px;
          mix-blend-mode: multiply;
          opacity: .7;
        }
        .spread::after {
          content: "";
          position: absolute;
          inset: 0;
          pointer-events: none;
          background:
            linear-gradient(to right, transparent calc(33.333% - .5px), rgba(0,0,0,.08) 33.333%, transparent calc(33.333% + .5px)),
            linear-gradient(to right, transparent calc(66.666% - .5px), rgba(0,0,0,.08) 66.666%, transparent calc(66.666% + .5px));
        }
        .panel {
          position: relative;
          padding: 64px 48px;
          display: flex;
          flex-direction: column;
          overflow: hidden;
        }
        .font-heading {
          font-family: var(--font-bricolage), 'Helvetica Neue', Helvetica, Arial, sans-serif;
        }
        @media print {
          html, body {
            background: #fff !important;
            padding: 0 !important;
            margin: 0 !important;
            min-height: 0 !important;
            -webkit-print-color-adjust: exact !important;
            print-color-adjust: exact !important;
          }
        }
      `}</style>
    </div>
  )
}

function Pill({ children }: { children: React.ReactNode }) {
  return (
    <span className="inline-flex items-center gap-2 px-3 py-1.5 rounded-full text-[13px] font-semibold tracking-[.02em] bg-[#E8ECFB] text-[#2A45D4]">
      <span className="w-1.5 h-1.5 rounded-full bg-[#2A45D4]" />
      {children}
    </span>
  )
}

function PanelFlap() {
  return (
    <div className="panel bg-[#EFEDE5]" style={{ padding: "72px 60px" }}>
      <div className="flex flex-col gap-4 mb-7">
        <Pill>Solapa · Inside flap · 01</Pill>
        <h2 className="font-heading font-bold text-[56px] leading-[.98] tracking-[-0.028em] text-[#0E1230]">
          Komando bakar batekin abian.
        </h2>
        <p className="text-[22px] text-[#1B2148] max-w-[540px] mt-2 leading-[1.4]">
          Ireki Docker, kopiatu errepoa eta exekutatu. Bost minututan, JavaFX langileentzat eta web atari publikoa, biak hartzaile bera duen datu-base baten gainean.
        </p>
      </div>

      {/* Quickstart terminal */}
      <div className="mt-7 bg-[#0E1230] text-[#EDEAE0] rounded-[14px] p-6 shadow-[0_20px_30px_-20px_rgba(0,0,0,.4)]">
        <div className="flex items-center gap-2.5 text-[14px] tracking-[.2em] uppercase text-[#9590a8] mb-4">
          <span className="inline-block w-1.5 h-1.5 bg-[#7CE0A9] rounded-full" />
          QUICKSTART · TERMINALA
        </div>
        <div className="font-mono text-[17px] leading-[1.75] text-white">
          <span className="text-[#9590a8]"># 1. Kopiatu eta sartu</span><br />
          <span className="text-[#7CE0A9]">$</span> git clone &lt;repo&gt; erronka-bermeo<br />
          <span className="text-[#7CE0A9]">$</span> cd erronka-bermeo<br />
          <span className="text-[#9590a8]"># 2. Edukiontzi guztiak abian (db · java · web · adminer)</span><br />
          <span className="text-[#7CE0A9]">$</span> docker compose up -d
        </div>
      </div>

      {/* Steps */}
      <div className="mt-9 flex flex-col gap-5">
        <Step num={1} title="JavaFX nabigatzailetik" desc="localhost:6080/vnc.html — VcXsrv edo X server-ik gabe, noVNC bidez. Linux eta Windows berdin." />
        <Step num={2} title="Web atari publikoa" desc="localhost:8000 — herritarrentzako katalogo responsive-a, XML+XSLT+XPath." />
        <Step num={3} title="Datu-basea Adminer-ekin" desc="localhost:8081 — taula guztiak, triggerrak, gordetako prozedurak ikusgai." />
      </div>

      {/* Credentials */}
      <div className="mt-9 bg-white border border-[#DDD9CC] rounded-[14px] overflow-hidden">
        <div className="px-6 py-4 bg-[#E8ECFB] text-[#2A45D4] font-bold text-[15px] tracking-[.16em] uppercase flex justify-between items-center">
          <span>Hasierako kredentzialak</span>
          <span className="font-mono text-[11px]">3 ROL</span>
        </div>
        <table className="w-full text-[17px] border-collapse">
          <tbody>
            <tr className="border-t border-[#DDD9CC]">
              <td className="px-6 py-4 text-[#5B628A] w-[42%]">Administratzailea</td>
              <td className="px-6 py-4 text-[#1B2148]"><span className="font-mono text-[16px]">admin</span> / <span className="font-mono text-[16px]">1234</span></td>
            </tr>
            <tr className="border-t border-[#DDD9CC]">
              <td className="px-6 py-4 text-[#5B628A]">Langilea</td>
              <td className="px-6 py-4 text-[#1B2148]"><span className="font-mono text-[16px]">langile1</span> / <span className="font-mono text-[16px]">1234</span></td>
            </tr>
            <tr className="border-t border-[#DDD9CC]">
              <td className="px-6 py-4 text-[#5B628A]">Ikuslea</td>
              <td className="px-6 py-4 text-[#1B2148]"><span className="font-mono text-[16px]">ikusle1</span> / <span className="font-mono text-[16px]">1234</span></td>
            </tr>
            <tr className="border-t border-[#DDD9CC]">
              <td className="px-6 py-4 text-[#5B628A]">Adminer</td>
              <td className="px-6 py-4 text-[#1B2148]"><span className="font-mono text-[16px]">admin</span> / <span className="font-mono text-[16px]">admin123</span></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  )
}

function Step({ num, title, desc }: { num: number; title: string; desc: string }) {
  return (
    <div className="grid grid-cols-[44px_1fr] gap-4 items-start">
      <div className="w-10 h-10 rounded-full bg-[#2A45D4] text-white flex items-center justify-center font-heading font-bold text-[20px]">
        {num}
      </div>
      <div>
        <h4 className="font-heading font-bold text-[21px] text-[#0E1230] tracking-[-0.01em] mb-1.5">{title}</h4>
        <p className="text-[16px] text-[#5B628A] leading-[1.5]">{desc}</p>
      </div>
    </div>
  )
}

function PanelBack() {
  return (
    <div className="panel text-[#EDEAE0]" style={{
      background: "linear-gradient(180deg, #11142F 0%, #0E1230 60%, #0A0E26 100%)",
      padding: "72px 56px",
      justifyContent: "space-between"
    }}>
      <div className="flex items-center justify-between text-[#9590a8] text-[17px] tracking-[.22em] uppercase font-semibold">
        <span><span className="inline-block w-2.5 h-2.5 bg-[#E2542C] rounded-full mr-3 align-middle" />Atzealdea · Back cover</span>
        <span>02</span>
      </div>

      <div className="mt-8">
        <h3 className="font-heading font-bold text-[56px] leading-[1.0] text-white max-w-[560px] mb-7 tracking-[-0.03em]">
          Hezkuntza-proiektua, bezeroaren benetako beharretik sortua.
        </h3>
        <p className="text-[#9590a8] max-w-[560px] text-[22px] leading-[1.5]">
          Erronka Bermeoko Udalarekin elkarlanean garatu da, herriko egiazko galera-aurkikuntza zerbitzua digitalizatzeko. Klase abstraktoa, MVC, dockerizazioa eta XML markatze-lengoaiak — guztia kode-base bakar batean.
        </p>
      </div>

      {/* Repo */}
      <div className="mt-10 border border-dashed border-white/20 rounded-[14px] p-6 flex flex-col gap-4">
        <div className="text-[16px] text-[#9590a8] tracking-[.22em] uppercase font-semibold">Iturri-kodea · GitHub</div>
        <div className="flex gap-4 items-center">
          <div className="w-[46px] h-[46px] rounded-[10px] bg-white/[0.06] flex items-center justify-center text-white">
            <GitHubIcon />
          </div>
          <div className="font-mono text-[17px] text-[#cfc9bd] break-all leading-[1.45]">
            github.com/<b className="text-white font-semibold">Garridoparrayeray</b>/<br />Talde14_Erronka_CIFPZornotzaLHII
          </div>
        </div>
        <div className="flex gap-4 items-center">
          <div className="w-[46px] h-[46px] rounded-[10px] bg-white/[0.06] flex items-center justify-center text-white">
            <GitHubIcon />
          </div>
          <div className="font-mono text-[17px] text-[#cfc9bd] break-all leading-[1.45]">
            github.com/<b className="text-white font-semibold">CIFPZornotzaLHII</b>/<br />reto-Garridoparrayeray
          </div>
        </div>
      </div>

      {/* Team */}
      <div className="mt-auto">
        <div className="text-[17px] tracking-[.22em] uppercase text-[#9590a8] font-semibold">Garapen-taldea · 1. DAW</div>
        <div className="grid gap-4 mt-9">
          <div className="flex flex-col gap-1.5 px-6 py-5 border border-white/[0.12] rounded-[14px] bg-white/[0.04]">
            <div className="font-heading font-bold text-[32px] text-white tracking-[-0.02em] leading-[1.1]">Yeray Garrido</div>
            <div className="text-[19px] text-[#9590a8] tracking-[.01em] font-mono">yeraygarrido.dev</div>
          </div>
          <div className="flex flex-col gap-1.5 px-6 py-5 border border-white/[0.12] rounded-[14px] bg-white/[0.04]">
            <div className="font-heading font-bold text-[32px] text-white tracking-[-0.02em] leading-[1.1]">Eder Martin</div>
            <div className="text-[19px] text-[#9590a8] tracking-[.01em] font-mono">github.com/emartinmos</div>
          </div>
        </div>
      </div>

      <div className="mt-11 pt-9 border-t border-white/[0.12] flex items-center justify-between gap-6">
        <div>
          <div className="text-[17px] tracking-[.18em] uppercase text-[#9590a8] font-semibold">Bezeroa</div>
          <div className="font-heading font-bold text-[28px] text-white tracking-[-0.02em] mt-2.5 leading-[1.1]">Bermeoko Udala</div>
          <div className="text-[17px] tracking-[.18em] uppercase text-[#9590a8] font-semibold mt-4">Ikastetxea</div>
          <div className="font-heading font-bold text-[28px] text-white tracking-[-0.02em] mt-2.5 leading-[1.1]">CIFP Zornotza LHII</div>
        </div>
        <QRCode />
      </div>
    </div>
  )
}

function PanelFront() {
  return (
    <div className="panel text-white" style={{
      background: `
        radial-gradient(900px 600px at 100% 0%, rgba(255,255,255,.08), transparent 60%),
        radial-gradient(700px 500px at 0% 100%, rgba(255,255,255,.05), transparent 60%),
        linear-gradient(180deg, #2F4BE0 0%, #2A45D4 50%, #1F33B0 100%)
      `,
      padding: "64px 60px 56px",
      justifyContent: "space-between"
    }}>
      <div className="flex items-start justify-between gap-6">
        <div className="bg-white rounded-[10px] px-3.5 py-2.5 flex items-center gap-2.5 text-[#0E1230]">
          <svg className="w-[34px] h-[42px]" viewBox="0 0 34 42" fill="none">
            <path d="M5 4 L29 6 L31 18 L26 24 L28 34 L18 40 L8 36 L4 28 L7 18 L2 12 Z" fill="#0E1230" stroke="#0E1230" strokeWidth="2" strokeLinejoin="round"/>
          </svg>
          <div className="leading-none font-heading font-extrabold tracking-[-0.02em]">
            <span className="text-[10px] tracking-[.22em] uppercase text-[#5B628A] font-semibold block">Bezeroa</span>
            <b className="text-[18px] block">BERMEOKO UDALA</b>
          </div>
        </div>
        <div className="border-[1.5px] border-white/50 rounded-full px-3.5 py-2 text-[11px] tracking-[.2em] uppercase font-semibold">
          03 · Azala
        </div>
      </div>

      <div>
        <div className="font-mono text-[13px] text-white/60 tracking-[.2em] uppercase mb-6">
          Galduen eta aurkituen kudeaketa
        </div>
        <div className="bg-white rounded-2xl p-12 shadow-[0_30px_50px_-25px_rgba(0,0,0,.45),0_0_0_1px_rgba(255,255,255,.18)]">
          <Image
            src="/images/logo-bermeoko-udala.png"
            alt="Bermeoko Udala"
            width={540}
            height={180}
            className="w-full h-auto max-w-[540px] mx-auto block"
          />
        </div>
        <div className="mt-9 font-heading font-medium text-[30px] leading-[1.15] max-w-[540px] tracking-[-0.015em]">
          Bermeoko Udalaren galdu eta aurkituen kudeaketa-sistema integratua.
        </div>
      </div>

      <div className="mt-10 pt-6 border-t border-white/[0.18] flex items-center justify-between text-[12px] tracking-[.18em] uppercase text-white/65">
        <span>v 1.0 · 2026</span>
        <span>1. DAW · CIFP Zornotza LHII</span>
      </div>
    </div>
  )
}

function PanelArchitecture() {
  return (
    <div className="panel bg-[#F6F4EE]">
      <span className="absolute top-6 right-6 font-mono text-[15px] text-[#5B628A] tracking-[.18em]">04 · Sistema</span>
      <div className="flex flex-col gap-2.5 mb-6">
        <span className="font-mono text-[#5B628A] text-[18px] tracking-[.18em]">— 01 / Arkitektura</span>
        <h2 className="font-heading font-bold text-[68px] leading-[.98] tracking-[-0.03em] text-[#0E1230]">
          Lau edukiontzi, datu-base partekatua.
        </h2>
        <p className="text-[#1B2148] text-[24px] max-w-[560px] mt-3.5 leading-[1.4]">
          Ekosistema osoa Docker Compose-ren gainean: JavaFX back-office, web atari publikoa, MariaDB eta Adminer. XML fitxategien bidez komunikatzen dira.
        </p>
      </div>

      {/* Architecture diagram */}
      <div className="mt-2 border border-[#DDD9CC] rounded-[14px] bg-white p-6">
        <div className="flex gap-3.5 justify-center">
          <ArchBox label="JavaFX App" sub="Langileak · noVNC" port=":6080" variant="javafx" />
          <ArchBox label="Web Portala" sub="Herritarrak · nginx" port=":8000" variant="web" />
        </div>
        <div className="text-center my-2.5 text-[#5B628A] font-mono text-[12px] leading-[1.1]">
          <div className="text-[14px] text-[#2A45D4] tracking-[6px]">↓ &nbsp; ↓</div>
          <div className="mt-1">artikuluak.xml · argazkiak</div>
        </div>
        <div className="flex justify-center">
          <ArchBox label="MariaDB 11" sub="erronka_galduak · 12 taula" port=":3306" variant="db" />
        </div>
        <div className="text-center my-2.5 text-[#5B628A] font-mono text-[12px]">
          <div className="text-[14px] text-[#2A45D4] tracking-[6px]">↓</div>
        </div>
        <div className="flex justify-center">
          <ArchBox label="Adminer" sub="DB-aren web UI" port=":8081" variant="adm" />
        </div>
      </div>

      {/* Stats */}
      <div className="flex gap-3.5 mt-4">
        <Stat num={4} label="Edukiontzi" />
        <Stat num={12} label="Taula" />
        <Stat num={3} label="DB rol" />
        <Stat num={5} label="SQL init" />
      </div>

      {/* Tech stack */}
      <div className="mt-5">
        <div className="text-[17px] tracking-[.22em] uppercase text-[#5B628A] font-semibold mb-3">Teknologia-pila</div>
        <div className="flex gap-2 flex-wrap">
          <Chip><b className="text-[#2A45D4]">Java 21</b> · JavaFX</Chip>
          <Chip>Maven · jpackage</Chip>
          <Chip><b className="text-[#2A45D4]">MariaDB</b> 11</Chip>
          <Chip>Nginx alpine</Chip>
          <Chip>Bootstrap 5</Chip>
          <Chip>XML · XSD · DTD</Chip>
          <Chip>XSLT · XPath · XQuery</Chip>
          <Chip><b className="text-[#2A45D4]">Docker</b> compose</Chip>
          <Chip>noVNC</Chip>
        </div>
      </div>

      {/* MVC */}
      <div className="mt-5">
        <div className="text-[17px] tracking-[.22em] uppercase text-[#5B628A] font-semibold mb-3.5">Patroia · MVC</div>
        <div className="grid grid-cols-3 gap-3 text-[16px]">
          <MVCBox title="Model" desc="Hartzailea abstraktoa, Jabea, Erakundea, Artikulua…" />
          <MVCBox title="View" desc="23 FXML pantaila + style.css." />
          <MVCBox title="Controller" desc="23 FXML kontrolatzaile + DAO + utils." />
        </div>
      </div>

      {/* Containers */}
      <div className="mt-auto pt-6">
        <div className="grid grid-cols-2 gap-2.5">
          <ContainerRow icon="db" title="erronka_db" sub="MariaDB 11 · 12 taula · 5 SQL init" port=":3306" />
          <ContainerRow icon="desktop" title="erronka_desktop" sub="JavaFX + noVNC · jpackage natiboa" port=":6080" />
          <ContainerRow icon="web" title="erronka_web" sub="Nginx alpine · XML+XSLT+XPath" port=":8000" />
          <ContainerRow icon="adminer" title="erronka_adminer" sub="DB-aren web UI · hutsetik konfiguratua" port=":8081" />
        </div>
      </div>
    </div>
  )
}

function PanelJavaFX() {
  return (
    <div className="panel bg-[#EFEDE5]">
      <span className="absolute top-6 right-6 font-mono text-[15px] text-[#5B628A] tracking-[.18em]">05 · Back-office</span>
      <div className="flex flex-col gap-2.5 mb-6">
        <span className="font-mono text-[#5B628A] text-[18px] tracking-[.18em]">— 02 / JavaFX · Langileak</span>
        <h2 className="font-heading font-bold text-[68px] leading-[.98] tracking-[-0.03em] text-[#0E1230]">
          Erregistrotik entregaraino, dena pantaila batean.
        </h2>
        <p className="text-[#1B2148] text-[24px] max-w-[560px] mt-3.5 leading-[1.4]">
          Bederatzi modulu udaleko langileentzat. Docker barruan edo .exe natiboa, biak GitHub errepositoriotik.
        </p>
      </div>

      {/* Feature chips */}
      <div className="grid grid-cols-2 gap-2 mt-2">
        <FChip>Erregistroa</FChip>
        <FChip>Inbentarioa</FChip>
        <FChip>Emanaldia</FChip>
        <FChip>Erreklamazioak</FChip>
        <FChip variant="warm">Iraungitakoak</FChip>
        <FChip>Auditoria</FChip>
        <FChip variant="alt">Admin panela</FChip>
        <FChip>Offline modua</FChip>
      </div>

      {/* Mockup */}
      <div className="mt-5 rounded-[12px] overflow-hidden bg-white border border-[#DDD9CC] shadow-[0_30px_40px_-30px_rgba(14,18,48,.35)]">
        <div className="flex items-center gap-1.5 px-3 py-2 bg-[#f2efe7] border-b border-[#DDD9CC]">
          <span className="w-2.5 h-2.5 rounded-full bg-[#E07A65]" />
          <span className="w-2.5 h-2.5 rounded-full bg-[#E8C26B]" />
          <span className="w-2.5 h-2.5 rounded-full bg-[#7CC192]" />
          <span className="ml-2 font-mono text-[11px] text-[#5B628A]">Bermeoko Udala — Galdutakoak · Panela</span>
        </div>
        <div className="bg-[#f8f7f1]">
          <Image
            src="/images/screen-dashboard.png"
            alt="JavaFX Panela"
            width={800}
            height={600}
            className="w-full h-auto block"
          />
        </div>
      </div>

      {/* Exec note */}
      <div className="mt-4 flex gap-3.5 items-start bg-[#0E1230] text-[#EDEAE0] rounded-[12px] p-4 shadow-[0_20px_30px_-20px_rgba(14,18,48,.4)]">
        <div className="w-[52px] h-[52px] rounded-[10px] bg-white/[0.08] flex items-center justify-center text-white shrink-0">
          <GitHubIcon />
        </div>
        <div>
          <div className="font-heading font-bold text-[26px] text-white tracking-[-0.02em] mb-2.5">
            Docker gabe ere: .exe natiboa
          </div>
          <div className="text-[19px] text-[#cfc9bd] leading-[1.5]">
            GitHub errepositoriotik <span className="font-mono text-[17px] bg-white/[0.08] px-1.5 py-0.5 rounded">Galdutakoak-1.0.exe</span> jaitsi daiteke — JVM barne darama, Java instalatu gabe exekutatzen da. <b className="text-white">Datu-baserako Docker beharrezkoa da</b>, MariaDB-arekin konektatzeko (<span className="font-mono text-[17px] bg-white/[0.08] px-1.5 py-0.5 rounded">localhost:3306</span>).
          </div>
        </div>
      </div>
    </div>
  )
}

function PanelWeb() {
  return (
    <div className="panel bg-[#F6F4EE]">
      <span className="absolute top-6 right-6 font-mono text-[15px] text-[#5B628A] tracking-[.18em]">06 · Front-office</span>
      <div className="flex flex-col gap-2.5 mb-6">
        <span className="font-mono text-[#5B628A] text-[18px] tracking-[.18em]">— 03 / Web atari · Herritarrak</span>
        <h2 className="font-heading font-bold text-[68px] leading-[.98] tracking-[-0.03em] text-[#0E1230]">
          Katalogoa eta erreklamazioak, denentzat eskuragarri.
        </h2>
        <p className="text-[#1B2148] text-[22px] max-w-[560px] mt-3.5 leading-[1.4]">
          Herritarrek galdutako objektuak bilatzaile edo kategoria bidez aurkitzeko ataria. Irisgarritasun estandarrak betez eta Google Insights-en errendimendu ezin hobearekin diseinatua.
        </p>
      </div>

      {/* Feature chips */}
      <div className="grid grid-cols-2 gap-2 mt-2">
        <FChip>Bilatzailea + Iragazkiak</FChip>
        <FChip>Kontakturako Form-a</FChip>
        <FChip>Bi hizkuntzetan (EU/ES)</FChip>
        <FChip variant="alt">Modu Iluna / Argia</FChip>
        <FChip>Vanilla JS (SPA)</FChip>
        <FChip>Irisgarritasuna</FChip>
        <FChip variant="warm">Google Insights %100</FChip>
      </div>

      {/* Browser mockup */}
      <div className="mt-4 rounded-[12px] overflow-hidden bg-white border border-[#DDD9CC] shadow-[0_30px_40px_-30px_rgba(14,18,48,.35)]">
        <div className="flex items-center gap-2 px-3 py-2 bg-[#f2efe7] border-b border-[#DDD9CC]">
          <span className="w-[9px] h-[9px] rounded-full bg-[#E07A65]" />
          <span className="w-[9px] h-[9px] rounded-full bg-[#E8C26B]" />
          <span className="w-[9px] h-[9px] rounded-full bg-[#7CC192]" />
          <div className="flex-1 bg-white border border-[#DDD9CC] rounded-full font-mono text-[11px] px-2.5 py-1 text-[#1B2148]">
            https://galduak.bermeo.eus/katalogoa
          </div>
        </div>
        <div className="bg-[#f8f7f1]">
          <Image
            src="/images/screen-reclamacion.png"
            alt="Galdutako objektua erreklamatu — formularioa"
            width={800}
            height={600}
            className="w-full h-auto block"
          />
        </div>
      </div>

      {/* Zero-Backend note */}
      <div className="mt-4 flex gap-3.5 items-start bg-[#1F8A5B] text-[#EDEAE0] rounded-[12px] p-4 shadow-[0_20px_30px_-20px_rgba(31,138,91,.4)]">
        <div className="w-[52px] h-[52px] rounded-[10px] bg-white/[0.15] flex items-center justify-center text-white shrink-0">
          <DownloadIcon />
        </div>
        <div>
          <div className="font-heading font-bold text-[26px] text-white tracking-[-0.02em] mb-1.5">
            Zero-Backend: XML Sortzailea
          </div>
          <div className="text-[16px] text-[#cfc9bd] leading-[1.5]">
            Erreklamazioa bidaltzean, JavaScript-ek datuak jaso eta <b className="text-white">XML dinamiko bat sortzen du memorian</b>. <span className="font-mono text-[13px] bg-white/[0.15] text-white px-1.5 py-0.5 rounded">erreklamazioa.xml</span> hori langileek <b className="text-white">Java aplikazioan inportatzeko</b> prestatuta dago, objektuen egoera erraz eguneratzeko.
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="mt-auto pt-4 border-t border-[#DDD9CC] flex items-center justify-between gap-3 font-mono text-[15px] text-[#5B628A]">
        <div className="flex items-center gap-2">
          <span className="inline-block w-3 h-3 rounded-full bg-[#1F8A5B]" />
          <span>galduak.bermeo.eus · :8000</span>
        </div>
        <span className="text-[13px] tracking-[.1em] uppercase">HTML5 · CSS3 · JS</span>
      </div>
    </div>
  )
}

// Helper components
function ArchBox({ label, sub, port, variant }: { label: string; sub: string; port: string; variant: "javafx" | "web" | "db" | "adm" }) {
  const styles = {
    javafx: "border-[#A9B3F0] bg-[#EEF0FC]",
    web: "border-[#F6CFC0] bg-[#FBEEE6]",
    db: "bg-[#0E1230] border-[#0E1230] text-white",
    adm: "bg-white border-dashed"
  }
  return (
    <div className={`flex-1 px-3.5 py-3 rounded-[10px] border text-center ${styles[variant]}`}>
      <div className={`font-heading font-bold text-[14px] ${variant === "db" ? "text-white" : "text-[#0E1230]"}`}>{label}</div>
      <div className={`text-[11px] mt-0.5 ${variant === "db" ? "text-[#a09cb0]" : "text-[#5B628A]"}`}>{sub}</div>
      <div className={`mt-2 font-mono text-[11px] inline-block px-2 py-0.5 rounded-[6px] border ${variant === "db" ? "bg-white/[0.08] border-transparent text-white" : "bg-white border-[#DDD9CC] text-[#1B2148]"}`}>
        {port}
      </div>
    </div>
  )
}

function Stat({ num, label }: { num: number; label: string }) {
  return (
    <div className="bg-white border border-[#DDD9CC] rounded-[10px] p-3 flex-1">
      <div className="font-heading font-extrabold text-[54px] text-[#2A45D4] leading-none tracking-[-0.035em]">{num}</div>
      <div className="text-[15px] tracking-[.18em] uppercase text-[#5B628A] mt-2.5">{label}</div>
    </div>
  )
}

function Chip({ children }: { children: React.ReactNode }) {
  return (
    <span className="px-4 py-3 rounded-full bg-white border border-[#DDD9CC] text-[18px] font-semibold text-[#1B2148]">
      {children}
    </span>
  )
}

function MVCBox({ title, desc }: { title: string; desc: string }) {
  return (
    <div className="bg-white border border-[#DDD9CC] rounded-[10px] px-4 py-4">
      <div className="text-[#2A45D4] font-heading font-bold text-[22px] tracking-[-0.01em]">{title}</div>
      <div className="text-[#5B628A] mt-1.5 text-[15px] leading-[1.4]">{desc}</div>
    </div>
  )
}

function ContainerRow({ icon, title, sub, port }: { icon: string; title: string; sub: string; port: string }) {
  return (
    <div className="flex items-center gap-3.5 px-4 py-3.5 bg-white border border-[#DDD9CC] rounded-[12px]">
      <div className="w-[52px] h-[52px] rounded-[10px] bg-[#E8ECFB] text-[#2A45D4] flex items-center justify-center shrink-0">
        <ContainerIcon type={icon} />
      </div>
      <div>
        <div className="font-bold text-[20px] leading-[1.2] tracking-[-0.01em] text-[#0E1230]">{title}</div>
        <div className="text-[15px] text-[#5B628A] mt-1">{sub}</div>
      </div>
      <div className="ml-auto font-mono text-[15px] text-[#1B2148] bg-[#EFEDE5] rounded-[6px] px-3 py-1.5 font-semibold">
        {port}
      </div>
    </div>
  )
}

function FChip({ children, variant }: { children: React.ReactNode; variant?: "alt" | "warm" }) {
  const dotColor = variant === "alt" ? "bg-[#0E1230]" : variant === "warm" ? "bg-[#E2542C]" : "bg-[#2A45D4]"
  return (
    <div className="flex items-center gap-3.5 bg-white border border-[#DDD9CC] rounded-[12px] px-5 py-4 text-[22px] font-semibold text-[#0E1230] tracking-[-0.01em]">
      <span className={`w-3 h-3 rounded-full ${dotColor} shrink-0`} />
      {children}
    </div>
  )
}

function ContainerIcon({ type }: { type: string }) {
  if (type === "db") {
    return (
      <svg className="w-[26px] h-[26px]" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <ellipse cx="12" cy="5" rx="8" ry="3"/>
        <path d="M4 5v6c0 1.7 3.6 3 8 3s8-1.3 8-3V5M4 11v6c0 1.7 3.6 3 8 3s8-1.3 8-3v-6"/>
      </svg>
    )
  }
  if (type === "desktop") {
    return (
      <svg className="w-[26px] h-[26px]" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="3" y="4" width="18" height="16" rx="2"/>
        <path d="M3 10h18"/>
      </svg>
    )
  }
  if (type === "web") {
    return (
      <svg className="w-[26px] h-[26px]" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="12" cy="12" r="9"/>
        <path d="M3 12h18M12 3a14 14 0 0 1 0 18M12 3a14 14 0 0 0 0 18"/>
      </svg>
    )
  }
  return (
    <svg className="w-[26px] h-[26px]" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 6h16v12H4z"/>
      <path d="M8 10h8M8 14h5"/>
    </svg>
  )
}

function GitHubIcon() {
  return (
    <svg className="w-[22px] h-[22px]" viewBox="0 0 24 24" fill="currentColor">
      <path d="M12 .5C5.65.5.5 5.65.5 12c0 5.08 3.29 9.39 7.86 10.91.58.11.79-.25.79-.56 0-.27-.01-1.16-.02-2.1-3.2.69-3.88-1.36-3.88-1.36-.52-1.33-1.27-1.68-1.27-1.68-1.04-.71.08-.7.08-.7 1.15.08 1.76 1.18 1.76 1.18 1.02 1.75 2.69 1.24 3.34.95.1-.74.4-1.24.73-1.53-2.55-.29-5.24-1.28-5.24-5.69 0-1.26.45-2.28 1.18-3.08-.12-.29-.51-1.46.11-3.04 0 0 .96-.31 3.15 1.18.91-.25 1.89-.38 2.86-.39.97.01 1.95.14 2.86.39 2.19-1.49 3.15-1.18 3.15-1.18.62 1.58.23 2.75.11 3.04.73.8 1.18 1.82 1.18 3.08 0 4.42-2.69 5.39-5.26 5.68.41.36.78 1.06.78 2.14 0 1.55-.01 2.79-.01 3.17 0 .31.21.68.8.56C20.21 21.39 23.5 17.08 23.5 12 23.5 5.65 18.35.5 12 .5z"/>
    </svg>
  )
}

function DownloadIcon() {
  return (
    <svg className="w-[24px] h-[24px]" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4M7 10l5 5 5-5M12 15V3"/>
    </svg>
  )
}

function QRCode() {
  return (
    <div className="w-[180px] h-[180px] bg-white rounded-[10px] p-3 flex items-center justify-center shrink-0">
      <svg viewBox="0 0 29 29" shapeRendering="crispEdges" className="w-full h-full">
        <rect width="29" height="29" fill="#fff"/>
        <g fill="#0E1230">
          <rect x="0" y="0" width="7" height="7"/>
          <rect x="1" y="1" width="5" height="5" fill="#fff"/>
          <rect x="2" y="2" width="3" height="3"/>
          <rect x="22" y="0" width="7" height="7"/>
          <rect x="23" y="1" width="5" height="5" fill="#fff"/>
          <rect x="24" y="2" width="3" height="3"/>
          <rect x="0" y="22" width="7" height="7"/>
          <rect x="1" y="23" width="5" height="5" fill="#fff"/>
          <rect x="2" y="24" width="3" height="3"/>
          <rect x="9" y="0" width="1" height="1"/><rect x="11" y="0" width="1" height="1"/><rect x="13" y="0" width="2" height="1"/><rect x="16" y="0" width="1" height="1"/><rect x="19" y="0" width="1" height="1"/>
          <rect x="9" y="2" width="2" height="1"/><rect x="13" y="2" width="1" height="1"/><rect x="15" y="2" width="2" height="1"/><rect x="19" y="2" width="2" height="1"/>
          <rect x="10" y="4" width="1" height="1"/><rect x="12" y="4" width="2" height="1"/><rect x="16" y="4" width="1" height="1"/><rect x="18" y="4" width="1" height="1"/><rect x="20" y="4" width="1" height="1"/>
        </g>
      </svg>
    </div>
  )
}
