module lang::rascalcore::check::Test5
alias Key = loc;
data IdRole;
data PathRole;
alias Define  = tuple[Key scope, str id, IdRole idRole, Key defined];
alias Defines = set[Define];
data TModel (
    Defines defines = {}
)   = tmodel()
    ;
private Key bind(TModel tm, Key scope, str id, set[IdRole] idRoles){
    defs = tm.defines[scope, id, idRoles];
}