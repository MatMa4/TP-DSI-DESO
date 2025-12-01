"use client";
import React, { useState } from "react";
import "../styles/stylesCU2.css";

type Huesped = {
  id?: number;
  nombre?: string;
  apellido?: string;
  tipoDocumento?: string;
  numeroDocumento?: string;
  [k: string]: any;
};

export default function BuscarHuespedPage() {
  const [nombre, setNombre] = useState("");
  const [apellido, setApellido] = useState("");
  const [tipoDocumento, setTipoDocumento] = useState("");
  const [numeroDocumento, setNumeroDocumento] = useState("");
  const [resultados, setResultados] = useState<Huesped[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const buildUrl = () => {
    const base = "http://localhost:8080/huespedes/buscar";
    const params: string[] = [];
    if (nombre.trim()) params.push(`nombre=${encodeURIComponent(nombre.trim())}`);
    if (apellido.trim()) params.push(`apellido=${encodeURIComponent(apellido.trim())}`);
    if (tipoDocumento.trim()) params.push(`tipoDocumento=${encodeURIComponent(tipoDocumento.trim())}`);
    if (numeroDocumento.trim()) params.push(`numeroDocumento=${encodeURIComponent(numeroDocumento.trim())}`);
    return params.length ? `${base}?${params.join("&")}` : base;
  };

  const buscar = async () => {
    setError(null);
    setLoading(true);
    setResultados([]);
    try {
      const url = buildUrl();
      const res = await fetch(url);
      if (!res.ok) throw new Error(`Error ${res.status}`);
      const data = await res.json();
      // Asumimos que el backend devuelve un arreglo de huespedes
      setResultados(Array.isArray(data) ? data : []);
    } catch (e: any) {
      setError(e.message || "Error al buscar huéspedes");
    } finally {
      setLoading(false);
    }
  };

  const cancelar = () => {
    setNombre("");
    setApellido("");
    setTipoDocumento("");
    setNumeroDocumento("");
    setResultados([]);
    setError(null);
  };

  return (
    <div className="main_box">
      <div className="tittle_box">Buscar Huésped</div>
      <div className="container">
        <div className="box1">
          <div className="box1_simplebox">
            <label>Nombre</label>
            <input
              className="input_box"
              value={nombre}
              onChange={(e) => setNombre(e.target.value)}
              placeholder="Nombre"
            />
          </div>

          <div className="box1_simplebox">
            <label>Apellido</label>
            <input
              className="input_box"
              value={apellido}
              onChange={(e) => setApellido(e.target.value)}
              placeholder="Apellido"
            />
          </div>

          <div className="box1_simplebox">
            <label>Documento</label>
            <div style={{ display: "flex", gap: 10, alignItems: "center" }}>
              <select
                className="select_box"
                value={tipoDocumento}
                onChange={(e) => setTipoDocumento(e.target.value)}
              >
                <option value="">Tipo</option>
                <option value="DNI">DNI</option>
                <option value="PASAPORTE">Pasaporte</option>
                <option value="CI">CI</option>
              </select>
              <input
                className="input_box"
                value={numeroDocumento}
                onChange={(e) => setNumeroDocumento(e.target.value)}
                placeholder="Número"
              />
            </div>
          </div>

          <div style={{ display: "flex", justifyContent: "space-between", marginTop: 20 }}>
            <button className="button_cancel" onClick={cancelar} type="button">
              Cancelar
            </button>
            <button className="button2" onClick={buscar} type="button" disabled={loading}>
              {loading ? "Buscando..." : "Buscar"}
            </button>
          </div>

          {error && <div className="error_text">{error}</div>}
        </div>

        <div className="right_panel">
          <h3 className="results_title">Resultados</h3>

          {resultados.length === 0 && !loading && (
            <div className="empty_state">No hay resultados</div>
          )}

          {resultados.map((h) => (
            <div key={h.id ?? `${h.tipoDocumento}-${h.numeroDocumento}-${Math.random()}`} className="result_card">
              <div className="result_name">
                {h.nombre ?? "-"} {h.apellido ?? "-"}
              </div>
              <div className="result_doc">
                {h.tipoDocumento ?? ""} {h.numeroDocumento ?? ""}
              </div>
              {/* Mostrar campos adicionales si existen */}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
