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
import ModalExitoEliminacion from '../components/ModalExitoEliminacion';

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
  
  // Modal Éxito Modificación (CU10)
  const [showSuccessModal, setShowSuccessModal] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  
  const [showCancelModal, setShowCancelModal] = useState(false);
  
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleteMessage, setDeleteMessage] = useState('');
  const [canDelete, setCanDelete] = useState(false);

  // Modal Éxito Eliminación (CU11)
  const [showDeleteSuccess, setShowDeleteSuccess] = useState(false);
  
  const [highlightDocumento, setHighlightDocumento] = useState(false);

  // --- CARGA DE DATOS ---
  useEffect(() => {
    const datosGuardados = localStorage.getItem('datosHuespedModificar');

    if (datosGuardados) {
        try {
            const data = JSON.parse(datosGuardados);
            
            // 🔍 LOG 1: DATOS QUE LLEGAN DEL CU2
            console.log("📥 [CU2 -> CU10] Datos Originales recibidos (LocalStorage):", data);

            const safeStr = (val: any) => (val === null || val === undefined) ? '' : val;

            const dataFormateada: FormData = {
                ...data,
                cuit: safeStr(data.cuit),
                posicionIVA: safeStr(data.posicionIVA),
                telefono: safeStr(data.telefono),
                email: safeStr(data.email),
                ocupacion: safeStr(data.ocupacion),
                nacionalidad: safeStr(data.nacionalidad),
                fechaNacimiento: data.fechaNacimiento ? data.fechaNacimiento.split('T')[0] : '',
                direccionHuesped: {
                    calle: safeStr(data.direccionHuesped?.calle),
                    numero: safeStr(data.direccionHuesped?.numero),
                    departamento: safeStr(data.direccionHuesped?.departamento),
                    piso: safeStr(data.direccionHuesped?.piso),
                    codigo: safeStr(data.direccionHuesped?.codigo),
                    localidad: safeStr(data.direccionHuesped?.localidad),
                    provincia: safeStr(data.direccionHuesped?.provincia),
                    pais: safeStr(data.direccionHuesped?.pais),
                }
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
      const valorFinal = type === 'checkbox' ? checked : (type === 'date' ? value : value.toUpperCase());
  
      if (name.startsWith('direccionHuesped.')) {
        const field = name.split('.')[1];
        setFormData((prev) => ({
           ...prev,
           direccionHuesped: { ...prev.direccionHuesped, [field]: valorFinal },
        }));
        setErrors((prev) => ({ ...prev, [name]: '' })); 
      } else {
        setFormData({ ...formData, [name]: valorFinal });
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

  // --- GUARDAR (CU10 - ACTUALIZAR) ---
  const guardarHuespedDirecto = async (dataAGuardar: any) => {
      const bodyPayload = [dataAGuardar, originalData];
      
      console.log("📡 --- INICIO PETICIÓN POST (CU10 Actualizar) ---");
      // 🔍 LOG 3: BODY COMPLETO
      console.log("📦 BODY QUE SE ENVÍA AL BACK:", JSON.stringify(bodyPayload, null, 2));

      try {
          const res = await fetch('http://localhost:8080/huespedes', {
            method: 'POST', 
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(bodyPayload),
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
          console.error(e);
          alert("Error de red.");
      }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors = validateHuespedForm(formData);
    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      
      // Función para limpiar textos
      const safeTrim = (value: any) => {
          if (value === null || value === undefined) return '';
          return value.toString().trim().toUpperCase();
      };

      const transformedData = {
        ...formData, 
        
        nombre: safeTrim(formData.nombre),
        apellido: safeTrim(formData.apellido),
        numeroDocumento: safeTrim(formData.numeroDocumento),
        tipoDocumento: formData.tipoDocumento,
        telefono: safeTrim(formData.telefono),
        email: safeTrim(formData.email),
        ocupacion: safeTrim(formData.ocupacion),
        nacionalidad: safeTrim(formData.nacionalidad),
        cuit: safeTrim(formData.cuit),
        posicionIVA: formData.posicionIVA ? safeTrim(formData.posicionIVA) : "CONSUMIDOR FINAL",
        fechaNacimiento: formData.fechaNacimiento,
        
        direccionHuesped: {
          ...formData.direccionHuesped, // Mantiene el ID
          calle: safeTrim(formData.direccionHuesped.calle),
          departamento: safeTrim(formData.direccionHuesped.departamento),
          localidad: safeTrim(formData.direccionHuesped.localidad),
          provincia: safeTrim(formData.direccionHuesped.provincia),
          pais: safeTrim(formData.direccionHuesped.pais),
          numero: safeTrim(formData.direccionHuesped.numero),
          piso: safeTrim(formData.direccionHuesped.piso),
          
          // --- REVERTIDO: SE ENVÍA COMO STRING (TEXTO) ---
          codigo: safeTrim(formData.direccionHuesped.codigo),
        }
      };

      // 🔍 LOG 2: HUESPED MODIFICADO
      console.log("📝 HUÉSPED MODIFICADO (Datos procesados del formulario):", transformedData);

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
            } else if (checkRes.status === 409) {
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
    setDeleteMessage(`¿Está seguro que desea eliminar del sistema al huésped ${formData.nombre} ${formData.apellido}?`);
    setCanDelete(true); 
    setShowDeleteModal(true);
  };

  const confirmDelete = async () => {
      const mostrarError = (mensaje: string) => {
        setShowDeleteModal(false); 
        setTimeout(() => {
            setDeleteMessage(mensaje);
            setCanDelete(false); 
            setShowDeleteModal(true);
        }, 100);
      };

      try {
          console.log("🗑️ Enviando DELETE con BODY:", originalData);
          
          const res = await fetch('http://localhost:8080/huespedes', { 
              method: 'DELETE',
              headers: { 'Content-Type': 'application/json' },
              body: JSON.stringify(originalData) 
          });

          if (res.ok) {
              localStorage.removeItem('datosHuespedModificar');
              setShowDeleteModal(false);
              setShowDeleteSuccess(true);
          } 
          else if (res.status === 409) {
              mostrarError("No se pudo eliminar al huésped porque tiene una factura a su nombre o registros asociados.");
          } 
          else if (res.status === 400) {
              mostrarError("El huésped se encuentra actualmente ALOJADO en el hotel y no puede ser eliminado.");
          }
          else if (res.status === 404) {
              mostrarError("Error: No se encontró al huésped en la base de datos (quizás ya fue eliminado).");
          }
          else {
              console.error("Error desconocido al eliminar:", res.status);
              mostrarError(`Ocurrió un error inesperado. Código: ${res.status}`);
          }

      } catch (e) {
          console.error(e);
          mostrarError("Error de conexión con el servidor.");
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

  const handleDeleteSuccessClose = () => {
      setShowDeleteSuccess(false);
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
          <DocumentoField tipoDocumento={formData.tipoDocumento} numeroDocumento={formData.numeroDocumento} onChange={handleChange} error={errors.numeroDocumento} highlight={highlightDocumento} />
        </div>

        <DireccionHuesped direccion={formData.direccionHuesped} onChange={handleChange} errors={errors} />

        <div className="container">
          <div className="box1"><InputField label="Email" name="email" value={formData.email} onChange={handleChange} error={errors.email} type="email" /></div>
          <div className="box1"><InputField label="Ocupación" name="ocupacion" value={formData.ocupacion} onChange={handleChange} error={errors.ocupacion} isRequired={true} /></div>
          <div className="box1"><InputField label="Nacionalidad" name="nacionalidad" value={formData.nacionalidad} onChange={handleChange} error={errors.nacionalidad} isRequired={true} /></div>
        </div>

        <div className="container" style={{ justifyContent: 'space-between', marginTop: '20px' }}>
          <div className="box1" style={{ flex: 0 }}>
             <button className="button2 red" type="button" onClick={handleBorrarClick} title="Eliminar huésped">BORRAR</button>
          </div>
          <div className="box1" style={{ display: 'flex', gap: '15px' }}>
            <button className="button2" type="button" onClick={handleCancelClick}>CANCELAR</button>
            <button className="button2" type="submit">SIGUIENTE</button>
          </div>
        </div>
      </form>

      {/* --- MODALES --- */}
          <ModalConfirmacion 
              show={showModal} title="¡CUIDADO!" message={modalMessage} icon="⚠️" 
              closeText="CORREGIR" confirmText="ACEPTAR IGUALMENTE" confirmClass="btn-accept yellow" 
              onClose={handleCloseConflict} onConfirm={handleConfirmConflict} 
          />

          <ModalConfirmacion 
              show={showCancelModal} title="CANCELAR" message="¿Desea cancelar la modificación?" icon="⚠️" 
              closeText="NO" confirmText="SI" confirmClass="btn-accept yellow" 
              onClose={() => setShowCancelModal(false)} onConfirm={handleConfirmCancel} 
          />

          <ModalExitoModificacion show={showSuccessModal} onConfirm={handleSuccessClose} />

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

          <ModalExitoEliminacion 
              show={showDeleteSuccess} 
              onConfirm={handleDeleteSuccessClose} 
          />
    </main>
  );
};