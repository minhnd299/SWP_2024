<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
                             <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Objective management</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#add">Add new</button>

                    <c:if test="${not empty sessionScope.notification}">
                        <div class="alert alert-success alert-dismissible fade show" role="alert" style="text-align: center">
                            ${sessionScope.notification}
                            <button type="button" class="btn-danger" data-dismiss="alert" aria-label="Close">x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notification");
                        %>
                    </c:if>
                    <c:if test="${not empty sessionScope.notificationErr}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert"  style="text-align: center">
                            ${sessionScope.notificationErr}
                            <button type="button" class="btn-danger" data-dismiss="alert" >x</button>
                        </div>
                        <%
                            // Clear the notification after displaying it
                            session.removeAttribute("notificationErr");
                        %>
                    </c:if>

                </div>
                <div class="card-body">
                    <div class="table-responsive">

                        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Description</th>
                                    <th>Created by</th>
                                    <th>Created At</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="o" items="${o}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${o.description}</td>
                                        <td>${o.lecturer.fullName}</td>
                                        <td>${o.createAt}</td>

                                        <td style="width: 250px">
                                            <a href="task?oid=${o.objective_id}" class="btn btn-info btn-sm">Detail</a>
                                            <button  type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#edit${o.objective_id}">Update</button>
                                            <button  type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${o.objective_id}">Delete</button>
                                        </td>
                                    </tr>
                                <div class="modal fade" id="edit${o.objective_id}" tabindex="-1" role="dialog" aria-labelledby="rejectModalLabel${o.objective_id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="rejectModalLabel${o.objective_id}">Reject Objective</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="objective" method="POST">
                                                <div class="modal-body">
                                                    <input class="form-control" type="hidden" name="action" value="edit" placeholder="Enter task title">
                                                    <label class="form-text">Description</label>
                                                    <textarea required class="form-control" style="height: 400px" name="description"  placeholder="Enter objective description">${o.description}</textarea>
                                                    <span id="descriptionError" style="color: red; font-size: smaller"></span>
                                                    <input type="hidden" name="action" value="edit">
                                                    <input type="hidden" name="id" value="${o.objective_id}">
                                                    <input class="form-control" type="hidden" name="cid" value="${param.cid}">
                                                </div>
                                                <div class="modal-footer">

                                                    <div class="modal-footer">
                                                        <button  type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-success btn-sm">Update</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal fade" id="delete${o.objective_id}" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel${o.objective_id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="deleteModalLabel${o.objective_id}">Delete Objective</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="objective" method="POST">
                                                <div class="modal-body">
                                                    Are you sure you want to delete this objective?
                                                </div>
                                                <div class="modal-footer">

                                                    <div class="modal-footer">

                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${o.objective_id}">
                                                        <input type="hidden" name="cid" value="${param.cid}">
                                                        <button  type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                        <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="add" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addModalLabel">Add new Objective</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form name="taskForm" id="taskForm" action="objective" method="POST" onsubmit="return validateForm()">
                    <div class="modal-body">
                        <input class="form-control" type="hidden" name="action" value="add" placeholder="Enter task title">
                        <input class="form-control" type="hidden" name="cid" value="${param.cid}">
                        <label class="form-text">Description</label>
                        <textarea class="form-control" style="height: 400px" name="description"  placeholder="Enter objective description"></textarea>
                        <span id="descriptionError" style="color: red; font-size: smaller"></span>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success btn-sm">Add</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function validateForm() {
        // Get form elements
        var description = document.forms["taskForm"]["description"].value.trim();

        var descriptionError = document.getElementById("descriptionError");

        // Reset error messages
        descriptionError.textContent = "";

        // Validate fields and set error messages

        if (description === "") {
            descriptionError.textContent = "Description must not be empty!";
            isValid = false;
        }

        return isValid;
    }
</script>

<jsp:include page="footer.jsp"></jsp:include>
