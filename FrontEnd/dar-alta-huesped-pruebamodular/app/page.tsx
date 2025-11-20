'use client';
import React from 'react';
// En tu proyecto real usa: import Link from 'next/link';
// Para la vista previa, usaremos una etiqueta <a> simple para evitar el error de resoluciónaaa . 

export default function Dashboard() {
  return (
    <main style={{ 
      backgroundColor: '#6B99C3', 
      minHeight: '100vh', 
      display: 'flex', 
      flexDirection: 'column', 
      alignItems: 'center', 
      justifyContent: 'center',
      fontFamily: 'sans-serif',
      color: 'white'
    }}>
      <h1 style={{ fontSize: '3rem', marginBottom: '40px' }}>🏨 Hotel Premier</h1>
      
      <div style={{ 
        display: 'grid', 
        gridTemplateColumns: 'repeat(2, 1fr)', 
        gap: '20px',
        maxWidth: '800px'
      }}>
        {/* CU 02 - Botón para ir a Buscar (y luego Alta) */}
        {/* En tu código local usa <Link href="..."> ... </Link> */}
        <a href="/buscar-huesped" style={cardStyle}>
          <div style={{ fontSize: '40px' }}>🔍</div>
          <h3>Buscar Huésped</h3>
          <p>CU 02 - Buscar, modificar o dar de alta nuevos huéspedes.</p>
        </a>

        {/* CU 09 - Acceso directo (Opcional) */}
        <a href="/darAltaHuesped" style={cardStyle}>
          <div style={{ fontSize: '40px' }}>👤</div>
          <h3>Nuevo Huésped</h3>
          <p>CU 09 - Registro directo de pasajero.</p>
        </a>

        {/* CU 04 - Reservas (Futuro) */}
        <a href="/reservar-habitacion" style={{...cardStyle, opacity: 0.7, cursor: 'not-allowed'}}>
          <div style={{ fontSize: '40px' }}>📅</div>
          <h3>Reservar</h3>
          <p>CU 04 - (Próximamente)</p>
        </a>

        {/* CU 05 - Estado (Futuro) */}
        <a href="/estado-habitaciones" style={{...cardStyle, opacity: 0.7, cursor: 'not-allowed'}}>
          <div style={{ fontSize: '40px' }}>🛏️</div>
          <h3>Estado Habitaciones</h3>
          <p>CU 05 - (Próximamente)</p>
        </a>
      </div>
    </main>
  );
}

// Definimos el tipo explícitamente para evitar errores de TypeScript
const cardStyle: React.CSSProperties = {
  backgroundColor: 'white',
  color: '#022E66',
  padding: '20px',
  borderRadius: '15px',
  textAlign: 'center',
  textDecoration: 'none',
  boxShadow: '0 4px 8px rgba(0,0,0,0.2)',
  transition: 'transform 0.2s',
  cursor: 'pointer',
  display: 'block' // Importante para que el <a> se comporte como bloque
};