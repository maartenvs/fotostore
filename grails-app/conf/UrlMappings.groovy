class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/limitedAccess/image/$key?" (controller: "limitedAccess", action: "image")

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
