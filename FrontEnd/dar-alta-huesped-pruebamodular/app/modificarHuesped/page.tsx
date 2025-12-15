'use client';
import '../styles/stylesCU9prueba.css'; 
import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';

// --- IMPORTACIONES DE COMPONENTES ---
import InputField from '../components/InputField';
import DocumentoField from '../components/DocumentoField';
import DireccionHuesped from '../components/DireccionHuesped';
import ModalConfirmacion from '../components/ModalConfirmacion';
import ModalExitoModificacion from '../components/ModalExitoModificacion';

// --- IMPORTACIONES DE TIPOS Y LÓGICA ---
import { FormData } from '../types';
import { validateHuespedForm } from '../darAltaHuesped/ValidacionLogicaErrores'; 

const INITIAL_FORM: FormData = {
  numeroDocumento: '', tipoDocumento: 'DNI', apellido: '', nombre: '',
  fechaNacimiento: '', telefono: '', email: '', ocupacion: '', nacionalidad: '',
  cuit: '', posicionIVA: '', alojado: false,
  direccionHuesped: {
    calle: '', numero: '', departamento: '', piso: '',
    codigo: '', localidad: '', provincia: '', pais: '',
  },
};

export default function ModificarHuesped() {
  const router = useRouter();

  // Estados
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM);
  const [originalData, setOriginalData] = useState<FormData>(INITIAL_FORM); 
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(true);

  // Modales
  const [showModal, setShowModal] = useState(false); 
  const [modalMessage, setModalMessage] = useState('');
  const [pendingFinalData, setPendingFinalData] = useState<any | null>(null);
  
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  
  const [showCancelModal, setShowCancelModal] = useState(false);
  
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteMessage, setDeleteMessage] = useState('');
  const [canDelete, setCanDelete] = useState(false);
  
  const [highlightDocumento, setHighlightDocumento] = useState(false);

  // --- CARGA DE DATOS DESDE LOCALSTORAGE ---
  useEffect(() => {
    const datosGuardados = localStorage.getItem('datosHuespedModificar');

    if (datosGuardados) {
        try {
            const data = JSON.parse(datosGuardados);
            const dataFormateada = {
                ...data,
                // Asegurar formato fecha para el input date (YYYY-MM-DD)
                fechaNacimiento: data.fechaNacimiento ? data.fechaNacimiento.split('T')[0] : '',
                direccionHuesped: data.direccionHuesped || INITIAL_FORM.direccionHuesped
            };

            setFormData(dataFormateada);
            setOriginalData(dataFormateada); 
        } catch (error) {
            console.error("Error al leer datos del storage", error);
            alert("Error al cargar los datos transferidos.");
            router.push('/');
        }
    } else {
        alert("No se ha seleccionado ningún huésped. Redirigiendo a búsqueda...");
        router.push('/');
    }
    setLoading(false);
  }, [router]);

  // --- MANEJADORES ---
  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
      const { name, value, type } = e.target;
      // @ts-ignore
      const checked = e.target.checked; 
      
      const valorFinal = type === 'checkbox' 
          ? checked 
          : (type === 'date' ? value : value.toUpperCase());
  
      if (name.startsWith('direccionHuesped.')) {
        const field = name.split('.')[1];
        setFormData((prev) => ({
           ...prev,
           direccionHuesped: {
             ...prev.direccionHuesped,
             [field]: valorFinal,
           },
        }));
        setErrors((prev) => ({ ...prev, [name]: '' })); 
      } else {
        setFormData({
          ...formData,
          [name]: valorFinal,
        });
        setErrors((prev) => ({ ...prev, [name]: '' }));
  
        if (name === 'numeroDocumento' || name === 'tipoDocumento') {
          setHighlightDocumento(false);
        }
      }
  };

  const handleCancelClick = (e: React.MouseEvent) => {
    e.preventDefault();
    setShowCancelModal(true);
  };

  // --- GUARDAR (PUT - CU10) ---
  const guardarHuespedDirecto = async (dataAGuardar: any) => {
      console.log("📡 Enviando PUT al Backend con estos datos:", dataAGuardar);

      try {
          const res = await fetch('http://localhost:8080/huespedes', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dataAGuardar),
          });

          if (res.ok) {
            setSuccessMessage('La operación ha culminado con éxito.'); 
            setShowSuccessModal(true);
            setPendingFinalData(null);
            localStorage.removeItem('datosHuespedModificar');
          } else {
            console.error("Error Backend:", res.status);
            alert("No se pudo actualizar el huésped.");
          }
      } catch (e) {
          alert("Error de red.");
      }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors = validateHuespedForm(formData);
    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      // 1. Preparar datos (Trim/Upper)
      const transformedData = {
        ...formData, 
        nombre: formData.nombre.trim(),
        apellido: formData.apellido.trim(),
        numeroDocumento: formData.numeroDocumento.trim(),
        tipoDocumento: formData.tipoDocumento,
        telefono: formData.telefono.trim(),
        email: formData.email.trim(),
        ocupacion: formData.ocupacion.trim(),
        nacionalidad: formData.nacionalidad.trim(),
        cuit: formData.cuit.trim(),
        posicionIVA: formData.posicionIVA.trim() ? formData.posicionIVA.trim() : "CONSUMIDOR FINAL",
        fechaNacimiento: formData.fechaNacimiento,
        // @ts-ignore
        id: formData.id, 
        
        direccionHuesped: {
          calle: formData.direccionHuesped.calle.trim(),
          departamento: formData.direccionHuesped.departamento.trim(),
          localidad: formData.direccionHuesped.localidad.trim(),
          provincia: formData.direccionHuesped.provincia.trim(),
          pais: formData.direccionHuesped.pais.trim(),
          numero: formData.direccionHuesped.numero,
          piso: formData.direccionHuesped.piso,
          codigo: formData.direccionHuesped.codigo,
        }
      };

      // 2. LÓGICA DE CAMBIO DE DNI
      const documentoCambio = 
          transformedData.tipoDocumento !== originalData.tipoDocumento || 
          transformedData.numeroDocumento !== originalData.numeroDocumento;

      if (documentoCambio) {
          try {
            const params = new URLSearchParams();
            params.append('tipo', transformedData.tipoDocumento);
            params.append('numero', transformedData.numeroDocumento);
            
            const checkRes = await fetch(`http://localhost:8080/huespedes/consultarDocumento?${params.toString()}`);

            if (checkRes.ok) {
                await guardarHuespedDirecto(transformedData);
            } 
            else if (checkRes.status === 409) {
                setPendingFinalData(transformedData); 
                setModalMessage(`¡CUIDADO! El tipo y número de documento ya existen en el sistema.`);
                setShowModal(true);
            }
          } catch (err) {
            console.error(err);
            alert("Error conectando con servidor para validar documento.");
          }
      } else {
          await guardarHuespedDirecto(transformedData);
      }
    } 
  };

  // --- LÓGICA BORRAR (CU11) ---
  const handleBorrarClick = () => {
    // Protección extra: Si está alojado, no hace nada
    if (formData.alojado) return;

    setDeleteMessage(`¿Está seguro que desea eliminar del sistema al huésped ${formData.nombre} ${formData.apellido}?`);
    setCanDelete(true); 
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
      try {
          console.log("🗑️ Enviando DELETE con BODY:", formData);
          
          // --- VERSIÓN CON BODY (RESTAURADA) ---
          const res = await fetch('http://localhost:8080/huespedes', { 
              method: 'DELETE',
              headers: { 
                  'Content-Type': 'application/json' 
              },
              body: JSON.stringify(formData) // <--- Se envía el DTO
          });

          if (res.ok) {
              localStorage.removeItem('datosHuespedModificar');
              setShowDeleteModal(false);
              alert("Huésped eliminado correctamente.");
              router.push('/menuCU1'); 
          } 
          else if (res.status === 409) {
              // ERROR 409: Conflicto por historial
              setShowDeleteModal(false); 
              setTimeout(() => {
                  setDeleteMessage("El huésped NO puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad (Integridad Referencial).");
                  setCanDelete(false); 
                  setShowDeleteModal(true);
              }, 100);
          } 
          else {
              console.error("Error al eliminar:", res.status);
              alert(`Ocurrió un error al intentar eliminar. Código: ${res.status}`);
          }

      } catch (e) {
          console.error(e);
          alert("Error de conexión con el servidor.");
      }
  };

  // --- CIERRES MODALES ---
  const handleConfirmConflict = async () => {
    setShowModal(false);
    setHighlightDocumento(false);
    if (pendingFinalData) await guardarHuespedDirecto(pendingFinalData);
  };
  
  const handleCloseConflict = () => {
    setHighlightDocumento(true);
    setShowModal(false);
  };

  const handleConfirmCancel = () => {
      localStorage.removeItem('datosHuespedModificar');
      router.back();
  };
  const handleSuccessClose = () => {
      setShowSuccessModal(false);
      router.push('/menuCU1');
  };

  if (loading) return <div className="main_box"><p>Cargando datos...</p></div>;

  return (
    <main>
      <div className="tittle_box"><h1 className="main_title">Modificar Huésped</h1></div>
      <form className="main_box" onSubmit={handleSubmit} noValidate>
        
        <div className="container">
          <div className="box1">
            <InputField label="Nombre" name="nombre" value={formData.nombre} onChange={handleChange} error={errors.nombre} isRequired={true} />
            <InputField label="CUIT" name="cuit" value={formData.cuit} onChange={handleChange} error={errors.cuit} />
            <InputField label="Fecha de Nacimiento" name="fechaNacimiento" value={formData.fechaNacimiento} onChange={handleChange} error={errors.fechaNacimiento} type="date" isRequired={true} />
          </div>
          <div className="box1">
            <InputField label="Apellido" name="apellido" value={formData.apellido} onChange={handleChange} error={errors.apellido} isRequired={true} />
            <InputField label="Teléfono" name="telefono" value={formData.telefono} onChange={handleChange} error={errors.telefono} type="tel" isRequired={true} />
            <InputField label="Posición IVA" name="posicionIVA" value={formData.posicionIVA} onChange={handleChange} error={errors.posicionIVA} />
          </div>
          <DocumentoField 
             tipoDocumento={formData.tipoDocumento} 
             numeroDocumento={formData.numeroDocumento} 
             onChange={handleChange} 
             error={errors.numeroDocumento} 
             highlight={highlightDocumento} 
          />
        </div>

        <DireccionHuesped direccion={formData.direccionHuesped} onChange={handleChange} errors={errors} />

        <div className="container">
          <div className="box1"><InputField label="Email" name="email" value={formData.email} onChange={handleChange} error={errors.email} type="email" /></div>
          <div className="box1"><InputField label="Ocupación" name="ocupacion" value={formData.ocupacion} onChange={handleChange} error={errors.ocupacion} isRequired={true} /></div>
          <div className="box1"><InputField label="Nacionalidad" name="nacionalidad" value={formData.nacionalidad} onChange={handleChange} error={errors.nacionalidad} isRequired={true} /></div>
        </div>

        <div className="container" style={{ justifyContent: 'space-between', marginTop: '20px' }}>
          <div className="box1" style={{ flex: 0 }}>
             
             {/* BOTÓN BORRAR ACTUALIZADO */}
             <button 
                className="button2 red" 
                type="button" 
                onClick={handleBorrarClick}
                disabled={formData.alojado} // <--- Bloquea la acción
                title={formData.alojado ? "No se puede borrar un huésped alojado." : "Eliminar huésped"}
                style={{
                    // Estilos dinámicos para Gris vs Rojo
                    backgroundColor: formData.alojado ? '#555555' : undefined,
                    borderColor: formData.alojado ? '#444444' : undefined,
                    cursor: formData.alojado ? 'not-allowed' : 'pointer',
                    opacity: formData.alojado ? 0.7 : 1
                }}
             >
                BORRAR
             </button>

          </div>
          <div className="box1" style={{ display: 'flex', gap: '15px' }}>
            <button className="button2" type="button" onClick={handleCancelClick}>CANCELAR</button>
            <button className="button2" type="submit">SIGUIENTE</button>
          </div>
        </div>
      </form>

      {/* --- MODALES --- */}
          <ModalConfirmacion 
              show={showModal} 
              title="¡CUIDADO!" 
              message={modalMessage} 
              icon="⚠️" 
              closeText="CORREGIR" 
              confirmText="ACEPTAR IGUALMENTE" 
              confirmClass="btn-accept yellow" 
              onClose={handleCloseConflict} 
              onConfirm={handleConfirmConflict} 
          />

          <ModalConfirmacion 
              show={showCancelModal} 
              title="CANCELAR" 
              message="¿Desea cancelar la modificación?" 
              icon="⚠️" 
              closeText="NO" 
              confirmText="SI" 
              confirmClass="btn-accept yellow" 
              onClose={() => setShowCancelModal(false)} 
              onConfirm={handleConfirmCancel} 
          />

          <ModalExitoModificacion
              show={showSuccessModal} 
              onConfirm={handleSuccessClose} 
          />

          <ModalConfirmacion 
              show={showDeleteModal} 
              title={canDelete ? "ELIMINAR HUÉSPED" : "NO SE PUEDE ELIMINAR"} 
              message={deleteMessage} 
              icon={canDelete ? "🗑️" : "⛔"} 
              closeText={canDelete ? "CANCELAR" : "CONTINUAR"} 
              confirmText={canDelete ? "ELIMINAR" : ""} 
              confirmClass={canDelete ? "btn-accept red" : "hidden"} 
              onClose={() => setShowDeleteModal(false)} 
              onConfirm={canDelete ? confirmDelete : undefined} 
          />
    </main>
  );
};