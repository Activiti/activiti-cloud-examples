db.createUser(
    {
        user: "segmentsUser",
        pwd: "segmentsPassword",
        roles: [
            {
                role: "readWrite",
                db: "segments"
            }
        ]
    }
);