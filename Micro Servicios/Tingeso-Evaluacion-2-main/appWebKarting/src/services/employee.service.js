import httClient from "../http-common";

const EMPLEADO_API_URL = "/api/usuarios"


const loginEmployee = (data) => {
    return httClient.post(`${EMPLEADO_API_URL}/login`, data);
};

const generarReservaEmpleado = (data) => {
    return httClient.post(`${EMPLEADO_API_URL}/generarReservaAdmin`, data)
}

export default { loginEmployee, generarReservaEmpleado };
