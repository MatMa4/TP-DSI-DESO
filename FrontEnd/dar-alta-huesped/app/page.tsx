import '../styles.css';
export default function Home() {
  return (
    <main>
       <div className="tittle_box">
          <h1 className="main_title">Completar los datos</h1>
        </div>

      <form className="main_box">
        <div className="container">
          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Nombre">Nombre *</label>
              <input className="input_box" type="text" required />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="CUIT">CUIT</label>
              <input className="input_box" type="text" />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="Fecha">Fecha de Nacimiento *</label>
              <input type="date" required />
            </div>
          </div>

          <div className="box1">
            <div className="box1_simplebox">
              <label htmlFor="Apellido">Apellido *</label>
              <input className="input_box" type="text" />
            </div>

            <div className="box1_simplebox">
              <label htmlFor="IVA">IVA *</label>
              <input className="input_box" type="text" required />
            </div>

            <div className="box1_simplebox">
              <label>Teléfono *</label>
              <input className="input_box" type="tel" name="telefono" required />
            </div>
          </div>

          <div className="box1 boxDocumento">
            <label>Documento *</label>
            <select className="input_box" name="tipoDocumento" required>
              <option value="dni">DNI</option>
              <option value="LE">LE</option>
              <option value="LC">LC</option>
              <option value="pasaporte">Pasaporte</option>
              <option value="Otro">Otro</option>
            </select>
            <input className="input_box_documento" type="text" name="numeroDocumento" required />
            
          </div>
        </div>

         <div className='container'>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Calle *</label>
                    <input className="input_box" type="text" name="calle" required />
                </div>

                <div className="box1_simplebox">
                    <label>Codigo Postal</label>
                    <input className="input_box" type="text" name="Codigo_Postal" required />
                </div>

            </div>

            <div className="box1">
                    
                    <div className="box1_simplebox">
                        <label>Número *</label>
                        <input  className="input_box" type="text" name="numero" required />
                    </div>
                    <div className="box1_simplebox">
                        <label>Localidad *</label>
                        <input  className="input_box" type="text" name="localidad" required />    
                    </div>
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Piso *</label>
                    <input className="input_box" type="text" name="piso" required />
                </div>
                <div className="box1_simplebox">
                    <label>Provincia *</label>
                    <input className="input_box" type="text" name="provincia" required />
                </div>

                
            </div>
            <div className="box1">
                <div className="box1_simplebox">
                    <label>Departamento *</label>
                    <input className="input_box" type="text" name="departamento" required />
                </div>
                <div className="box1_simplebox">
                    <label>País *</label>
                    <input className="input_box" type="text" name="pais" required />
                </div>
            </div>
        </div>
        <div className="container">
            <div className="box1">
                <label>Email</label>
                <input className="input_box" type="email" name="email" />    
            </div>

            <div className="box1">
                <label>Ocupación *</label>
                <input  className="input_box" type="text" name="ocupacion" required />    
            </div>

            <div className="box1">
                <label>Nacionalidad *</label>
                <input className="input_box" type="text" name="nacionalidad" required />
            </div>         
        </div>    
        
        
        


        <div className="container">
            <div className="box1">
                <button className="button2" type="reset">CANCELAR</button>
            </div>
            <div className="box1">
                <p><small><span style={{color: 'red'}}>*</span> Campos obligatorios</small></p>
            </div>
            <div className="box1">
                <button className="button2" type="submit">SIGUIENTE</button>    
            </div>
        </div> 
      </form>
    </main>
  );
}