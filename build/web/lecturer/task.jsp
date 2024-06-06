<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"></jsp:include>
    <div id="content-wrapper" class="d-flex flex-column">
        <div id="content">
                       <jsp:include page="header-content.jsp"></jsp:include>

            <div class="container-fluid">
                <h1 class="h3 mb-2 text-gray-800">Task management</h1>
                <div class="card shadow mb-4">
                    <div class="card-header py-3">
                        <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#add">Add new</button>
                        <li class="nav-item dropdown" style="float: right">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
                               role="button" data-toggle="dropdown" aria-haspopup="true"
                               aria-expanded="false">
                                Filter by status
                            </a>
                            <div class="dropdown-menu dropdown-menu-right animated--grow-in"
                                 aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="task?oid=${param.oid}">All</a>
                            <a class="dropdown-item" href="task?oid=${param.oid}&AMP;status=in progress">In progress</a>
                            <a class="dropdown-item" href="task?oid=${param.oid}&AMP;status=not started">Not started</a>
                            <a class="dropdown-item" href="task?oid=${param.oid}&AMP;status=done">Done</a>
                        </div>
                    </li>
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
                                    <th>Title</th>
                                    <th>Description</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="t" items="${task}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>${t.title}</td>
                                        <td>${t.description}</td>
                                        <td>${t.start_date}</td>
                                        <td>${t.end_date}</td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${t.status == 'in progress'}">
                                                    <button class="btn btn-info btn-sm">
                                                        <span class="text">${t.status}</span>
                                                    </button>
                                                </c:when>
                                                <c:when test="${t.status == 'done'}">
                                                    <button class="btn btn-success btn-sm">
                                                        <span class="text">${t.status}</span>
                                                    </button>
                                                </c:when>

                                                <c:otherwise>
                                                    <button class="btn btn-secondary btn-sm">
                                                        <span class="text">${t.status}</span>
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>

                                        <td style="width: 199px">
                                            <c:choose>
                                                <c:when test="${t.status == 'in progress'}">
                                                    <button class="btn btn-warning btn-sm" data-toggle="modal" data-target="#edit${t.task_id}">
                                                        Update 
                                                    </button>
                                                    <button class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${t.task_id}">
                                                        Delete 
                                                    </button>
                                                    <a href="task-detail?id=${t.task_id}" class="btn btn-info btn-sm">Detail</a>
                                                </c:when>
                                                <c:when test="${t.status == 'done'}">
                                                    <a href="task-detail?id=${t.task_id}" class="btn btn-info btn-sm">Detail</a>
                                                    <button class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${t.task_id}">
                                                        Delete 
                                                    </button>
                                                </c:when>

                                                <c:otherwise>
                                                    <button class="btn btn-warning btn-sm" data-toggle="modal" data-target="#edit${t.task_id}">
                                                        Update 
                                                    </button>
                                                    <button class="btn btn-danger btn-sm" data-toggle="modal" data-target="#delete${t.task_id}">
                                                        Delete 
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>

                                        </td>
                                    </tr>

                                <div class="modal fade" id="edit${t.task_id}" tabindex="-1" role="dialog" aria-labelledby="addModalLabel" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="addModalLabel">Update Task</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form name="taskForm" id="taskForm" action="task" method="POST" onsubmit="return validateForm()">
                                                <div class="modal-body">
                                                    <label class="form-text">Title</label>
                                                    <input class="form-control" name="title" type="text" value="${t.title}" placeholder="Enter task title">
                                                    <span id="titleError" style="color: red; font-size: smaller"></span>
                                                    <input class="form-control" type="hidden" name="oid" value="${param.oid}">
                                                    <input class="form-control" type="hidden" name="id" value="${t.task_id}">
                                                    <input class="form-control" type="hidden" name="action" value="update" >
                                                    <hr>

                                                    <label class="form-text">Start date</label>
                                                    <input class="form-control" name="start_date" type="date" value="${t.start_date}" placeholder="Enter task title">
                                                    <label class="form-text">End date</label>
                                                    <input class="form-control" name="end_date" type="date" value="${t.end_date}" placeholder="Enter task title">

                                                    <label class="form-text">Description</label>
                                                    <textarea class="form-control" name="description" placeholder="Enter task description">${t.description}</textarea>
                                                    <span id="descriptionError" style="color: red; font-size: smaller"></span>
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-secondary btn-sm" data-dismiss="modal">Cancel</button>
                                                    <button type="submit" class="btn btn-success btn-sm">Update</button>
                                                </div>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                                <div class="modal fade" id="delete${t.task_id}" tabindex="-1" role="dialog" aria-labelledby="deleteModalLabel${t.task_id}" aria-hidden="true">
                                    <div class="modal-dialog" role="document">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="deleteModalLabel${t.task_id}">Delete Task</h5>
                                                <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                                                    <span aria-hidden="true">×</span>
                                                </button>
                                            </div>
                                            <form action="task" method="POST">
                                                <div class="modal-body">
                                                    Are you sure you want to delete this task?
                                                </div>
                                                <div class="modal-footer">

                                                    <div class="modal-footer">

                                                        <input type="hidden" name="action" value="delete">
                                                        <input type="hidden" name="id" value="${t.task_id}">
                                                        <input type="hidden" name="oid" value="${param.oid}">
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
                    <h5 class="modal-title" id="addModalLabel">Add new Task</h5>
                    <button class="close" type="button" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <form name="taskForm" id="taskForm" action="task" method="POST" onsubmit="return validateForm()">
                    <div class="modal-body">
                        <label class="form-text">Title</label>
                        <input class="form-control" required="" name="title" type="text" placeholder="Enter task title">
                        <span id="titleError" style="color: red; font-size: smaller"></span>

                        <input class="form-control" type="hidden" name="oid" value="${param.oid}" placeholder="Enter task title">
                        <input class="form-control" type="hidden" name="action" value="add" placeholder="Enter task title">

                        <hr>
                        <label class="form-text">Description</label>
                        <input class="form-control" required name="description" type="text" placeholder="Enter task description">
                        <span id="descriptionError" style="color: red; font-size: smaller"></span>

                        <label class="form-text">Start Date</label>
                        <input class="form-control" required name="start_date" type="date" placeholder="Enter start date">
                        <span id="startDateError" style="color: red; font-size: smaller"></span>

                        <label class="form-text">End Date</label>
                        <input class="form-control" required name="end_date" type="date" placeholder="Enter end date">
                        <span id="endDateError" style="color: red; font-size: smaller"></span>
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


<jsp:include page="footer.jsp"></jsp:include>
