-- INSERT INTO FIRMA_ABOGADOS VALUES (
--     1,
--     'Criterion'
-- );

INSERT INTO PERSONA VALUES (
    '1004870909',
    'contraseña1',
    'direccion1',
    'correo1@gmail.com',
    'Juan',
    'juan123',
    '3136142910',
    1
);

INSERT INTO PERSONA VALUES (
    '1004870908',
    'contraseña2',
    'direccion2',
    'correo2@gmail.com',
    'Pedro',
    'pedro123',
    '3136142911',
    1
);

INSERT INTO PERSONA VALUES (
    '1004870907',
    'contraseña3',
    'direccion3',
    'correo3@gmail.com',
    'Carlos',
    'carlos123',
    '3136142912',
    1
);

INSERT INTO PERSONA VALUES (
    '1004870906',
    'contraseña4',
    'direccion4',
    'correo4@gmail.com',
    'Maria',
    'maria123',
    '3136142913',
    1
);

INSERT INTO PERSONA VALUES (
    '1004870905',
    'contraseña5',
    'direccion5',
    'correo5@gmail.com',
    'Luisa',
    'luisa123',
    '3136142914',
    1
);

INSERT INTO ABOGADO VALUES (
    'CIVIL',
    CURRENT_DATE,
    '123456789',
    '1004870909'
);

INSERT INTO ADMIN VALUES (
    '1004870908'
);


INSERT INTO ASESOR VALUES (
    '1004870907'    
);

INSERT INTO CLIENTE VALUES (
    '1004870906',
    '1004870907'
);

INSERT INTO ABOGADO VALUES (
    'CIVIL',
    CURRENT_DATE,
    '123456788',
    '1004870905'
);

INSERT INTO CASO VALUES (
    1,
    'descripcion1',
    'CIVIL',
    'ABIERTO',
    CURRENT_DATE,
    TO_DATE('2023-11-15', 'YYYY-MM-DD'),
    '1004870909',
    '1004870907',
    '1004870906',
    1
);

INSERT INTO DOCUMENTO VALUES (
    1,
    'documento1',
    CURRENT_DATE,
    'Titulo1',
    1

);

INSERT INTO FACTURA VALUES(
    1,
    1,
    CURRENT_DATE,
    3000.00,
    1
);

INSERT INTO REUNION VALUES(
    1,
    CURRENT_DATE,
    'notas1',
    'pereira',
    1
);
