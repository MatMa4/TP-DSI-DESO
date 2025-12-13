// src/app/test-facturacion/page.jsx
"use client"; // Importante para que funcionen los hooks (useState)

import React from 'react';
import FacturacionGeneral from '../facturar/FacturacionGeneral'; 
// Asegúrate de que la ruta de importación sea correcta según donde guardaste el componente

export default function Page() {
  return (
    <div>
      {/* Renderizamos el componente directamente aquí */}
      <FacturacionGeneral />
    </div>
  );
}